package com.crossfit.crossfitapplication.service.error

import org.springframework.http.HttpStatus

data class KeyCloakErrorResponse(val errorMessage: String, val status: HttpStatus)
