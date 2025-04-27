package com.crossfit.crossfitapplication.datasource.keycloak

import com.crossfit.crossfitapplication.datasource.error.DataSourceError
import com.crossfit.crossfitapplication.datasource.error.enums.DataSourceErrorRetryPolicy
import com.crossfit.crossfitapplication.datasource.keycloak.configuration.KeycloakProperties
import com.crossfit.crossfitapplication.datasource.keycloak.response.KeycloakTokenResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.michaelbull.result.*
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import kotlinx.coroutines.reactor.awaitSingle
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import java.net.URI
import java.time.Duration
import java.util.*

@Service
class KeycloakDataSource(
    private val realmResource: RealmResource, private val webClientBuilder: WebClient.Builder, private val keycloakProperties: KeycloakProperties,
) {

    companion object {
        private val logger = KotlinLogging.logger {}
        private val objectMapper = ObjectMapper()
    }

    private val webClient by lazy { webClientBuilder.build() }

    /**
     * Registers a new user in Keycloak using a functional error handling approach.
     */
    fun registerUserOnKeycloak(username: String, email: String, firstName: String, lastName: String, password: String): Result<String, DataSourceError> {
        logger.debug { "Attempting to register user '$username' in Keycloak (functional approach)." }
        val userRepresentation = UserRepresentation().apply {
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

        // Step 1: Try creating the user, catch any initial errors
        return runCatching {
            realmResource.users().create(userRepresentation) // Returns Response
        }.mapError {
            mapThrowableToDataSourceError(it)
        }.andThen { response ->
            val status = response.status
            if (status == Response.Status.CREATED.statusCode) { // 201
                val locationHeader: URI? = response.location
                val createdUserId = locationHeader?.path?.substringAfterLast('/')
                response.close() // Always close after reading

                if (createdUserId != null) {
                    logger.info { "User '$username' successfully created. ID: $createdUserId" }
                    Ok(createdUserId)
                } else {
                    // Fallback search (less ideal)
                    logger.warn { "Unable to extract User ID from Location header for '$username'. Attempting search..." }
                    runCatching {
                        realmResource.users().searchByUsername(username, true)
                    }.mapError {
                        mapThrowableToDataSourceError(it)
                    }.andThen { users ->
                        if (users.isNotEmpty()) {
                            val foundId = users[0].id
                            logger.info { "User '$username' found via search. ID: $foundId" }
                            Ok(foundId)
                        } else {
                            logger.error { "User '$username' created (status 201) but not found via search!" }
                            Err(DataSourceError(errorMessage = "User created but ID could not be retrieved", httpStatus = HttpStatus.INTERNAL_SERVER_ERROR))
                        }
                    }
                }
            } else {
                val errorDetails = readErrorResponse(response)
                logger.error { "Failed to create user '$username'. Status: $status, Response: ${errorDetails.errorMessage}" }
                Err(errorDetails)
            }
        }
    }

    /**
     * Deletes a user from Keycloak using a functional error handling approach.
     */
    fun deleteUserByKeycloakId(userId: String): Result<Unit, DataSourceError> {
        logger.debug { "Attempting to delete Keycloak user with ID: $userId (functional approach)." }
        return runCatching {
            realmResource.users().get(userId)?.remove()
                ?: throw NotFoundException("User with ID $userId not found for deletion.")
        }.mapError { throwable ->
            mapThrowableToDataSourceError(throwable)
        }.andThen {
            logger.info { "Successfully deleted Keycloak user with ID: $userId." }
            Ok(Unit)
        }
    }

    /**
     * Resets the password for a given Keycloak user using a functional error handling approach.
     */
    fun resetUserPassword(userId: String, newPassword: String, temporary: Boolean = false): Result<Unit, DataSourceError> {
        logger.debug { "Attempting to reset password for Keycloak user ID: $userId (functional approach)." }
        val credential = CredentialRepresentation().apply {
            type = CredentialRepresentation.PASSWORD
            value = newPassword
            isTemporary = temporary
        }

        return runCatching {
            val userResource = realmResource.users().get(userId)
                ?: throw NotFoundException("User with ID $userId not found for password reset.")
            userResource.resetPassword(credential)
        }.mapError { throwable ->
            mapThrowableToDataSourceError(throwable)
        }.andThen {
            logger.info { "Successfully reset password for Keycloak user ID: $userId." }
            Ok(Unit)
        }
    }

    /**
     * Helper function for reading error details from a Keycloak response.
     * IMPORTANT: Always closes the Response after reading.
     */
    fun readErrorResponse(response: Response): DataSourceError {
        val status = response.status
        var message = "Status $status, unknown error."

        try {
            if (response.hasEntity()) {
                val jsonString = response.readEntity(String::class.java)
                if (!jsonString.isNullOrBlank()) {
                    message = try {
                        val jsonNode = objectMapper.readTree(jsonString)
                        val errorMsgNode = jsonNode.path("errorMessage")
                        if (!errorMsgNode.isMissingNode && errorMsgNode.isTextual) {
                            errorMsgNode.asText()
                        } else {
                            logger.warn { "Keycloak error response JSON does not contain a textual 'errorMessage' field. Body: $jsonString" }
                            jsonString
                        }
                    } catch (e: JsonProcessingException) {
                        logger.warn(e) { "Failed to parse Keycloak error response as JSON. Body: $jsonString" }
                        jsonString
                    }
                } else {
                    message = "Status $status, empty response body."
                }
            } else {
                message = "Status $status, no response body."
            }
        } catch (e: Exception) {
            logger.error(e) { "Error while reading response body: ${e.message}" }
            message = "Status $status, unable to read error body: ${e.message}"
        } finally {
            response.close()
        }
        return DataSourceError(errorMessage = message, httpStatus = HttpStatus.valueOf(status))
    }

    /**
     * Helper function to map any Throwable to a DataSourceError.
     */
    private fun mapThrowableToDataSourceError(throwable: Throwable): DataSourceError {
        logger.error(throwable) { "Exception caught during Keycloak call: ${throwable.message}" }
        return when (throwable) {
            is NotFoundException -> {
                DataSourceError(
                    errorMessage = "Requested resource not found",
                    httpStatus = HttpStatus.NOT_FOUND,
                    retryPolicy = DataSourceErrorRetryPolicy.NOT_RETRYABLE,
                )
            }

            is WebApplicationException -> {
                val response = throwable.response
                val status = response?.status ?: 500
                val message = try {
                    response?.readEntity(String::class.java) ?: throwable.message ?: "Unknown WebApplicationException"
                } catch (readError: Exception) {
                    throwable.message ?: "Unknown WebApplicationException (failed to read body)"
                } finally {
                    response?.close()
                }
                DataSourceError(
                    errorMessage = "Keycloak API call error: $message",
                    httpStatus = HttpStatus.valueOf(status),
                    retryPolicy = DataSourceErrorRetryPolicy.RETRYABLE,
                )
            }

            else -> {
                DataSourceError(
                    errorMessage = "Unexpected error: ${throwable.message}",
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                    retryPolicy = DataSourceErrorRetryPolicy.RETRYABLE,
                )
            }
        }
    }

    suspend fun authenticate(username: String, password: String): Result<KeycloakTokenResponse, DataSourceError> {
        return runCatching {
            val tokenUri = "${keycloakProperties.provider.keycloak.issuerUri}/protocol/openid-connect/token"

            val formData: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
                add("grant_type", "password")
                add("client_id", keycloakProperties.registration.keycloak.clientId)
                add("client_secret", keycloakProperties.registration.keycloak.clientSecret)
                add("username", username)
                add("password", password)
            }

            webClient.post()
                .uri(tokenUri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .onStatus({ it.isError }) { response ->
                    response.bodyToMono(String::class.java).flatMap { body ->
                        Mono.error(
                            WebClientResponseException.create(
                                response.statusCode(),
                                "Error from Keycloak: ${response.statusCode()} - $body",
                                response.headers().asHttpHeaders(),
                                body.toByteArray(),
                                null,
                                null,
                            ),
                        )
                    }
                }
                .bodyToMono(KeycloakTokenResponse::class.java) // DTO klasa
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1)))
                .awaitSingle()
        }.mapError {
            mapThrowableToDataSourceError(it)
        }
    }
}
