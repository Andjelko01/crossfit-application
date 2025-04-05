package com.crossfit.crossfitapplication.service.error

import com.crossfit.crossfitapplication.service.error.enums.ServiceErrorRetryPolicy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.ws.rs.core.Response
import org.springframework.http.HttpStatus

fun Response.extractErrorDetails(): KeyCloakErrorResponse {
    return try {
        val objectMapper = jacksonObjectMapper()

        val responseBody = this.readEntity(String::class.java)
        val rawResponse: Map<String, Any> = objectMapper.readValue(responseBody)
        val errorMessage = rawResponse["errorMessage"] as? String ?: "Unknown error"

        val statusCode = this.status
        val httpStatus = HttpStatus.resolve(statusCode) ?: HttpStatus.INTERNAL_SERVER_ERROR

        KeyCloakErrorResponse(errorMessage, httpStatus)
    } catch (e: Exception) {
        KeyCloakErrorResponse(
            errorMessage = "Failed to extract error details: ${e.message}",
            status = HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}

fun KeyCloakErrorResponse.toRetryPolicy(): ServiceErrorRetryPolicy {
    return when (this.status) {
        HttpStatus.INTERNAL_SERVER_ERROR, // 500
        HttpStatus.BAD_GATEWAY, // 502
        HttpStatus.SERVICE_UNAVAILABLE, // 503
        HttpStatus.GATEWAY_TIMEOUT, // 504
        HttpStatus.TOO_MANY_REQUESTS, // 429
        -> ServiceErrorRetryPolicy.RETRYABLE

        else -> ServiceErrorRetryPolicy.NOT_RETRYABLE
    }
}
