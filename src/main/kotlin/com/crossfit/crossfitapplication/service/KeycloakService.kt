package com.crossfit.crossfitapplication.service

import com.crossfit.crossfitapplication.application.controller.request.MemberCreateRequest
import com.crossfit.crossfitapplication.datasource.keycloak.KeycloakDataSource
import com.crossfit.crossfitapplication.service.error.ServiceError
import com.crossfit.crossfitapplication.service.mapper.toServiceError
import com.github.michaelbull.result.*
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
            logger.error { "Greška prilikom registracije na Keycloak: $originalError" }
            originalError.toServiceError()
        }.andThen { keycloakUserId ->
            logger.info { "User registered on Keycloak (ID: $keycloakUserId). Attempting to save locally." }
            Ok(keycloakUserId)
        }
    }
}
