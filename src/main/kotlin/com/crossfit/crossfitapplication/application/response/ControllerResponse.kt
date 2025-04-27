package com.crossfit.crossfitapplication.application.response

import org.springframework.http.HttpStatus

data class ControllerResponse<T>(
    val status: HttpStatus?,
    val message: String?,
    val data: T? = null,
    val error: String? = null,
)
