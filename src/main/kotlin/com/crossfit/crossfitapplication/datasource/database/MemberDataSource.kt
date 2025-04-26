package com.crossfit.crossfitapplication.datasource.database

import com.crossfit.crossfitapplication.datasource.database.jpa.MemberRepository
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import com.crossfit.crossfitapplication.datasource.error.DataSourceError
import com.crossfit.crossfitapplication.datasource.error.enums.DataSourceErrorRetryPolicy
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import jakarta.persistence.QueryTimeoutException
import org.hibernate.TransactionException
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.dao.RecoverableDataAccessException
import org.springframework.dao.TransientDataAccessResourceException
import org.springframework.stereotype.Service
import java.sql.SQLTransientException

@Service
class MemberDataSource(private val memberRepository: MemberRepository) {

    fun createMember(member: Member):Result<Member, DataSourceError> {
        return com.github.michaelbull.result.runCatching {
            memberRepository.save(member)
        }.mapError { error ->
            handleException(error)
        }
    }

    fun deleteMember(keycloakId: String):Result<Unit, DataSourceError> {
        return com.github.michaelbull.result.runCatching {
            memberRepository.deleteMemberByKeycloakId(keycloakId)
        }.mapError { error ->
            handleException(error)
        }
    }

    private fun handleException(exception: Throwable): DataSourceError {
        val retryPolicy =
            when (exception) {
                is QueryTimeoutException,
                is TransientDataAccessResourceException,
                is ConcurrencyFailureException,
                is RecoverableDataAccessException,
                is SQLTransientException,
                is TransactionException,
                    -> DataSourceErrorRetryPolicy.RETRYABLE

                else -> DataSourceErrorRetryPolicy.NOT_RETRYABLE
            }

        return DataSourceError(retryPolicy, "Problem with database: ${exception.message}")
    }

}