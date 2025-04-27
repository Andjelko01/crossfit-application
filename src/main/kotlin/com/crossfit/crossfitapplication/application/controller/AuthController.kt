package com.crossfit.crossfitapplication.application.controller

import com.crossfit.crossfitapplication.application.controller.request.member.MemberLoginRequest
import com.crossfit.crossfitapplication.application.controller.response.ControllerResponse
import com.crossfit.crossfitapplication.service.KeycloakService
import com.crossfit.crossfitapplication.service.common.SecurityHelper
import com.github.michaelbull.result.fold
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

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

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    fun protectedEndpoint(): String {
        val userKeycloakId = securityHelper.getCurrentUserKeycloakId()
        return "Access granted to protected endpoint!"
    }


}