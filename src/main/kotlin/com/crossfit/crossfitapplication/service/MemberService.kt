package com.crossfit.crossfitapplication.service

import com.crossfit.crossfitapplication.application.controller.request.MemberCreateRequest
import com.crossfit.crossfitapplication.service.error.ServiceError
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MemberService() {
    @Autowired
    private lateinit var keycloakService: KeycloakService

    fun createMember(memberRequest: MemberCreateRequest): Result<String, ServiceError> {
        keycloakService.createMember(memberRequest).fold(
            success = { keycloakUserId ->
                return Ok(keycloakUserId)
            },
            failure = { error ->
                return Err(error)
            },
        )
    }

//    fun deleteMember(Id: String): Result<Unit, ServiceError> {
//        keycloakService.deleteUserByKeycloakId(
//        Id
//        ).fold(
//            success = {
//                return Ok(it)
//            },
//            failure = { error ->
//                return Err(error)
//            },
//        )
//    }
}
