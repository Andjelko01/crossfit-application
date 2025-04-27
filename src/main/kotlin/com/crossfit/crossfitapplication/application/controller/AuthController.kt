package com.crossfit.crossfitapplication.application.controller

import com.crossfit.crossfitapplication.application.request.member.MemberLoginRequest
import com.crossfit.crossfitapplication.application.response.ControllerResponse
import com.crossfit.crossfitapplication.service.KeycloakService
import com.crossfit.crossfitapplication.service.common.SecurityHelper
import com.github.michaelbull.result.fold
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val keycloakService: KeycloakService, private val securityHelper: SecurityHelper) {

    @PostMapping("/token")
    suspend fun getAuthToken(
        @RequestBody memberLoginRequest: MemberLoginRequest
    ): ResponseEntity<ControllerResponse<Any>> {
        keycloakService.getToken(memberLoginRequest.username, memberLoginRequest.password).fold(
            success = {
                return ResponseEntity.ok(ControllerResponse(HttpStatus.OK, "Access token", data = it))
            },
            failure = { error ->
                return ResponseEntity.status(error.httpStatus!!.value()).body(
                    ControllerResponse(
                        status = error.httpStatus,
                        message = error.errorMessage,
                        error = error.retryPolicy!!.name,
                    ),
                )
            },
        )
    }
}