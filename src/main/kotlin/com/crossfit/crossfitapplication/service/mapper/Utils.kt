package com.crossfit.crossfitapplication.service.mapper

import com.crossfit.crossfitapplication.datasource.error.DataSourceError
import com.crossfit.crossfitapplication.service.error.ServiceError
import com.crossfit.crossfitapplication.service.error.enums.ServiceErrorRetryPolicy

fun DataSourceError.toServiceError(): ServiceError {
    return ServiceError(retryPolicy = this.retryPolicy?.let { ServiceErrorRetryPolicy.valueOf(it.name) }, httpStatus = this.httpStatus, errorMessage = this.errorMessage)
}
