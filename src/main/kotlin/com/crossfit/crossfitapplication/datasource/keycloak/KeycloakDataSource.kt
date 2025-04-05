package com.crossfit.crossfitapplication.datasource.keycloak

import com.crossfit.crossfitapplication.datasource.error.DataSourceError
import com.crossfit.crossfitapplication.datasource.error.enums.DataSourceErrorRetryPolicy
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.michaelbull.result.*
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.URI

@Service
class KeycloakDataSource {

    @Autowired
    private lateinit var realmResource: RealmResource

    companion object {
        private val logger = KotlinLogging.logger {}
        private val objectMapper = ObjectMapper()

    }

    /**
     * Registruje novog korisnika u Keycloak-u koristeći funkcionalni pristup za greške.
     */
    fun registerUserOnKeycloak(username: String, email: String, firstName: String, lastName: String, password: String): Result<String, DataSourceError> {
        logger.debug { "Pokušaj registracije korisnika '$username' na Keycloak (funkcionalni pristup)." }
        val userRepresentation = UserRepresentation().apply {
            // ... (isto kao ranije) ...
            this.username = username
            this.email = email
            this.firstName = firstName
            this.lastName = lastName
            this.isEnabled = true
            this.credentials = listOf(
                CredentialRepresentation().apply {
                    this.type = CredentialRepresentation.PASSWORD
                    this.value = password
                    this.isTemporary = false
                },
            )
        }

        // Korak 1: Pokušaj kreiranja korisnika, uhvati inicijalne greške
        return runCatching {
            realmResource.users().create(userRepresentation) // Vraća Response
        }.mapError {
            // Mapiraj greške koje su se desile TOKOM create() poziva
            mapThrowableToDataSourceError(it)
        }.andThen { response -> // Ako je create() uspeo (bez izuzetka), obradi Response
            val status = response.status
            if (status == Response.Status.CREATED.statusCode) { // 201
                val locationHeader: URI? = response.location
                val createdUserId = locationHeader?.path?.substringAfterLast('/')
                response.close() // Zatvori response NAKON čitanja headera

                if (createdUserId != null) {
                    logger.info { "Korisnik '$username' uspešno kreiran. ID: $createdUserId" }
                    Ok(createdUserId)
                } else {
                    // Fallback pretraga (manje idealno)
                    logger.warn { "Nije moguće izvući User ID iz Location header-a za '$username'. Pokušaj pretrage..." }
                    runCatching {
                        realmResource.users().searchByUsername(username, true)
                    }.mapError {
                        mapThrowableToDataSourceError(it) // Mapiraj grešku pretrage
                    }.andThen { users ->
                        if (users.isNotEmpty()) {
                            val foundId = users[0].id
                            logger.info { "Korisnik '$username' pronađen pretragom. ID: $foundId" }
                            Ok(foundId)
                        } else {
                            logger.error { "Korisnik '$username' kreiran (status 201) ali nije pronađen pretragom!" }
                            Err(DataSourceError(errorMessage = "Korisnik kreiran ali ID nije mogao biti dohvaćen", httpStatus = HttpStatus.INTERNAL_SERVER_ERROR))
                        }
                    }
                }
            } else {
                // Status nije 201, ekstraktuj grešku iz response-a
                val errorDetails = readErrorResponse(response) // Čita i zatvara response
                logger.error { "Neuspešno kreiranje korisnika '$username'. Status: $status, Odgovor: ${errorDetails.errorMessage}" }
                Err(errorDetails)
            }
        }
    }

    /**
     * Briše korisnika iz Keycloak-a koristeći funkcionalni pristup za greške.
     */
    fun deleteUserByKeycloakId(userId: String): Result<Unit, DataSourceError> {
        logger.debug { "Pokušaj brisanja Keycloak korisnika ID: $userId (funkcionalni pristup)." }
        // runCatching hvata bilo koji Throwable iz bloka
        return runCatching {
            // Dohvati UserResource i odmah pozovi remove()
            // Ako get() vrati null ili remove() baci izuzetak, biće uhvaćeno
            realmResource.users().get(userId)?.remove()
                ?: throw NotFoundException("Korisnik sa ID $userId nije pronađen za brisanje.") // Eksplicitno baci ako get vrati null
            // Ako nije bilo izuzetka, implicitno vraća Unit
        }.mapError { throwable ->
            // Mapiraj uhvaćeni Throwable u naš DataSourceError
            mapThrowableToDataSourceError(throwable)
        }.andThen {
            // Ako je runCatching uspeo (vratio Unit), loguj i vrati Ok(Unit)
            logger.info { "Uspešno obrisan Keycloak korisnik sa ID: $userId." }
            Ok(Unit)
        }
        // Nema potrebe za eksplicitnim Ok(Unit) van andThen jer mapError vraća Result<Nothing, DataSourceError>
        // a andThen transformiše Result<Unit, DataSourceError> -> Result<Unit, DataSourceError>
        // Ako runCatching vrati uspeh (Unit), a mapError se ne aktivira, rezultat je Ok(Unit)
    }

    /**
     * Resetuje lozinku za datog Keycloak korisnika koristeći funkcionalni pristup za greške.
     */
    fun resetUserPassword(userId: String, newPassword: String, temporary: Boolean = false): Result<Unit, DataSourceError> {
        logger.debug { "Pokušaj resetovanja lozinke za Keycloak korisnika ID: $userId (funkcionalni pristup)." }
        val credential = CredentialRepresentation().apply {
            type = CredentialRepresentation.PASSWORD
            value = newPassword
            isTemporary = temporary
        }

        return runCatching {
            realmResource.users().get(userId)?.resetPassword(credential)
                ?: throw NotFoundException("Korisnik sa ID $userId nije pronađen za reset lozinke.")
            // Vraća Unit ako uspe
        }.mapError { throwable ->
            mapThrowableToDataSourceError(throwable)
        }.andThen {
            logger.info { "Uspešno resetovana lozinka za Keycloak korisnika ID: $userId" }
            Ok(Unit) // Vrati Ok eksplicitno ako je potrebno radi jasnoće ili daljeg lanca
        }
    }

    // Helper funkcija za čitanje greške iz Keycloak odgovora (prilagodi po potrebi)
// Važno: Ova funkcija sada treba da prima Response i da ga OBAVEZNO zatvori.
    fun readErrorResponse(response: Response): DataSourceError {
        val status = response.status
        var message = "Status $status, nepoznata greška." // Default message

        try {
            if (response.hasEntity()) {
                val jsonString = response.readEntity(String::class.java)
                if (!jsonString.isNullOrBlank()) {
                    message = try {
                        // Attempt to parse as JSON and extract 'errorMessage'
                        val jsonNode = objectMapper.readTree(jsonString)
                        // Use .path() which returns a missing node instead of null, then check textValue()
                        val errorMsgNode = jsonNode.path("errorMessage")
                        if (!errorMsgNode.isMissingNode && errorMsgNode.isTextual) {
                            errorMsgNode.asText() // Extracted value!
                        } else {
                            // JSON valid, but 'errorMessage' key missing or not text
                            logger.warn { "Keycloak error response JSON does not contain a textual 'errorMessage' field. Body: $jsonString" }
                            jsonString // Fallback to the full JSON string
                        }
                    } catch (e: JsonProcessingException) {
                        // Body was not valid JSON, use the raw string as the message
                        logger.warn(e) { "Failed to parse Keycloak error response as JSON. Body: $jsonString" }
                        jsonString // Fallback to the full string if parsing fails
                    }
                } else {
                    message = "Status $status, prazno telo odgovora."
                }
            } else {
                message = "Status $status, nema tela odgovora."
            }
        } catch (e: Exception) {
            // Catch errors during readEntity itself
            logger.error(e) {"Greška prilikom čitanja tela odgovora: ${e.message}"}
            message = "Status $status, nije moguće pročitati telo greške: ${e.message}"
        } finally {
            // Ensure response is always closed
            response.close()
        }
        // Create DataSourceError with potentially extracted message
        return DataSourceError(errorMessage = message, httpStatus = HttpStatus.valueOf(status))
    }


    // === Helper funkcija za mapiranje Throwable u DataSourceError ===
    private fun mapThrowableToDataSourceError(throwable: Throwable): DataSourceError {
        logger.error(throwable) { "Greška uhvaćena u Keycloak pozivu: ${throwable.message}" }
        return when (throwable) {
            is NotFoundException -> {
                DataSourceError(
                    errorMessage = "Traženi resurs nije pronađen",
                    httpStatus = HttpStatus.NOT_FOUND,
                    retryPolicy = DataSourceErrorRetryPolicy.NOT_RETRYABLE,
                )
            }

            is WebApplicationException -> {
                val response = throwable.response
                val status = response?.status ?: 500
                val message = try {
                    response?.readEntity(String::class.java) ?: throwable.message ?: "Nepoznata WebApplicationException"
                } catch (readError: Exception) {
                    throwable.message ?: "Nepoznata WebApplicationException (čitanje tela neuspešno)"
                } finally {
                    response?.close() // Zatvori response ako postoji
                }
                DataSourceError(errorMessage = "Greška u Keycloak API pozivu: $message", retryPolicy = DataSourceErrorRetryPolicy.RETRYABLE, httpStatus = HttpStatus.valueOf(status))
            }

            else -> {
                DataSourceError(errorMessage = "Neočekivana greška: ${throwable.message}", httpStatus = HttpStatus.INTERNAL_SERVER_ERROR, retryPolicy = DataSourceErrorRetryPolicy.RETRYABLE)
            }
        }
    }
}
