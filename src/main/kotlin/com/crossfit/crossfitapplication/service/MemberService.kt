package com.crossfit.crossfitapplication.service

import com.crossfit.crossfitapplication.application.controller.request.member.MemberCreateRequest
import com.crossfit.crossfitapplication.datasource.database.MemberDataSource
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import com.crossfit.crossfitapplication.datasource.error.DataSourceError
import com.crossfit.crossfitapplication.datasource.error.enums.DataSourceErrorRetryPolicy
import com.crossfit.crossfitapplication.service.error.ServiceError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(private val keycloakService: KeycloakService, private val memberDataSource: MemberDataSource) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Transactional
    fun createMember(memberRequest: MemberCreateRequest): Result<String, DataSourceError> {
        return keycloakService.createMember(memberRequest).fold(
            success = { keycloakUserId ->
                    val databaseMember = Member(
                        firstName = memberRequest.firstName,
                        lastName = memberRequest.lastName,
                        email = memberRequest.email,
                        phoneNumber = memberRequest.phoneNumber,
                        dateOfBirth = memberRequest.dateOfBirth,
                        joinDate = memberRequest.joinDate,
                        username = memberRequest.username,
                        keycloakId = keycloakUserId
                    )
                    memberDataSource.createMember(databaseMember).fold(
                    success = {
                        Ok(keycloakUserId)
                    },
                    failure = { dbException ->
                        keycloakService.deleteUserByKeycloakId(keycloakUserId).fold(
                            success = {
                                logger.error { "COMPENSATION: Successfully deleted Keycloak user $keycloakUserId due to DB failure." }
                                Err(DataSourceError(DataSourceErrorRetryPolicy.RETRYABLE,dbException.errorMessage ?: "Unknown database error during createMember"))
                            },
                            failure = { compensationError ->
                                logger.error {"CRITICAL FAILURE: Could not compensate (delete) Keycloak user $keycloakUserId after DB failure. Keycloak Error: $compensationError. Original DB error: ${dbException.errorMessage}"}
                                Err(DataSourceError(DataSourceErrorRetryPolicy.RETRYABLE,"Failed to delete Keycloak user $keycloakUserId after DB error. Initial DB error: ${dbException.errorMessage}"))
                            }
                        )
                    }
                )
            },
            failure = { keycloakError ->
                Err(DataSourceError(DataSourceErrorRetryPolicy.RETRYABLE,keycloakError.errorMessage, httpStatus = HttpStatus.INTERNAL_SERVER_ERROR))
            }
        )
    }

    fun deleteMember(keycloakId: String): Result<Unit, ServiceError> {
        keycloakService.deleteUserByKeycloakId(keycloakId).fold(
            success = {
                memberDataSource.deleteMember(keycloakId)
                return Ok(it)
            },
            failure = { error ->
                return Err(error)
            },
        )
    }

    fun resetUserPassword(Id: String, newPassword: String): Result<Unit, ServiceError> {
        keycloakService.resetUserPassword(Id, newPassword).fold(
            success = {
                return Ok(it)
            },
            failure = { error ->
                return Err(error)
            },
        )
    }
}
