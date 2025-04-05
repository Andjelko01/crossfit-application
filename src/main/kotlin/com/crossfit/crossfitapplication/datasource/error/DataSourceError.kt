package com.crossfit.crossfitapplication.datasource.error

import com.crossfit.crossfitapplication.datasource.error.enums.DataSourceErrorRetryPolicy
import org.springframework.http.HttpStatus

class DataSourceError(val retryPolicy: DataSourceErrorRetryPolicy? = null, val errorMessage: String?, val httpStatus: HttpStatus?)
