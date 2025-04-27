package com.crossfit.crossfitapplication.service.error

import com.crossfit.crossfitapplication.service.error.enums.ServiceErrorRetryPolicy
import org.springframework.http.HttpStatus

class ServiceError(val retryPolicy: ServiceErrorRetryPolicy? = null, var errorMessage: String?, val httpStatus: HttpStatus?)
