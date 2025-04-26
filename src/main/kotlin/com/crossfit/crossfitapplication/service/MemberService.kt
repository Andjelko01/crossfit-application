package com.crossfit.crossfitapplication.service

import com.crossfit.crossfitapplication.application.controller.request.MemberCreateRequest
import com.crossfit.crossfitapplication.datasource.database.MemberDataSource
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import com.crossfit.crossfitapplication.service.error.ServiceError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import org.springframework.stereotype.Service

@Service
class MemberService(private val keycloakService: KeycloakService, private val memberDataSource: MemberDataSource) {

    fun createMember(memberRequest: MemberCreateRequest): Result<String, ServiceError> {
        keycloakService.createMember(memberRequest).fold(
            success = { keycloakUserId ->

                val databaseMember = Member(
                    firstName = memberRequest.firstName,
                    lastName = memberRequest.lastName,
                    email = memberRequest.email,
                    phoneNumber = memberRequest.phoneNumber,
                    dateOfBirth = memberRequest.dateOfBirth,
                    joinDate = memberRequest.joinDate,
                    keycloakId = keycloakUserId
                )

                memberDataSource.createMember(databaseMember)

                return Ok(keycloakUserId)
            },
            failure = { error ->
                return Err(error)
            },
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
