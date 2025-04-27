package com.crossfit.crossfitapplication.datasource.database.common

import com.crossfit.crossfitapplication.datasource.error.DataSourceError
import com.crossfit.crossfitapplication.datasource.error.enums.DataSourceErrorRetryPolicy
import jakarta.persistence.QueryTimeoutException
import org.hibernate.TransactionException
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.dao.RecoverableDataAccessException
import org.springframework.dao.TransientDataAccessResourceException
import java.sql.SQLTransientException

fun Throwable.toDataSourceError(): DataSourceError {
    val retryPolicy =
        when (this) {
            is QueryTimeoutException,
            is TransientDataAccessResourceException,
            is ConcurrencyFailureException,
            is RecoverableDataAccessException,
            is SQLTransientException,
            is TransactionException,
            -> DataSourceErrorRetryPolicy.RETRYABLE

            else -> DataSourceErrorRetryPolicy.NOT_RETRYABLE
        }

    return DataSourceError(retryPolicy, "Problem with database: ${this.message}")
}
