package com.crossfit.crossfitapplication.service

import com.crossfit.crossfitapplication.application.request.member.MemberCreateRequest
import com.crossfit.crossfitapplication.datasource.keycloak.KeycloakDataSource
import com.crossfit.crossfitapplication.service.error.ServiceError
import com.crossfit.crossfitapplication.service.mapper.toServiceError
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KeycloakService {

    @Autowired
    lateinit var keycloakDataSource: KeycloakDataSource

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    fun createMember(memberRequest: MemberCreateRequest): Result<String, ServiceError> {
        return keycloakDataSource.registerUserOnKeycloak(
            username = memberRequest.username,
            email = memberRequest.email,
            firstName = memberRequest.firstName,
            lastName = memberRequest.lastName,
            password = memberRequest.password,
        ).mapError { originalError ->
            logger.error { "Error during registration on Keycloak: ${originalError.errorMessage}" }
            originalError.toServiceError()
        }.andThen { keycloakUserId ->
            logger.info { "User registered on Keycloak (ID: $keycloakUserId). Attempting to save locally." }
            Ok(keycloakUserId)
        }
    }

    fun deleteUserByKeycloakId(userId: String): Result<Unit, ServiceError> {
        return keycloakDataSource.deleteUserByKeycloakId(userId).mapError { originalError ->
            logger.error { "Error during user deletion on Keycloak: $originalError" }
            originalError.toServiceError()
        }.andThen { keycloakUserId ->
            logger.info { "User deleted on Keycloak (ID: $keycloakUserId). Attempting to save locally." }
            Ok(keycloakUserId)
        }
    }

    fun resetUserPassword(userId: String, newPassword: String): Result<Unit, ServiceError> {
        return keycloakDataSource.resetUserPassword(userId, newPassword).mapError { originalError ->
            logger.error { "Error during user password reset on Keycloak: $originalError" }
            originalError.toServiceError()
        }.andThen { keycloakUserId ->
            logger.info { "User password reset on Keycloak (ID: $keycloakUserId). Attempting to save locally." }
            Ok(keycloakUserId)
        }
    }

    suspend fun getToken(username: String, password: String): Result<Any, ServiceError> {
        return keycloakDataSource.authenticate(username, password).mapError { originalError ->
            logger.error { "Error while fetching user access token from Keycloak: $originalError" }
            originalError.toServiceError()
        }.andThen { keycloakUserId ->
            logger.info { "User access token fetched successfully from Keycloak" }
            Ok(keycloakUserId)
        }
    }

}
