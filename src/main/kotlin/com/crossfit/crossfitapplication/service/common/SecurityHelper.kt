package com.crossfit.crossfitapplication.service.common

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class SecurityHelper {

    fun getCurrentUserKeycloakId(): String {
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val jwt = authentication.token as Jwt
        return jwt.subject
    }
}