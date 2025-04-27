package com.crossfit.crossfitapplication.datasource.database

import com.crossfit.crossfitapplication.datasource.database.common.toDataSourceError
import com.crossfit.crossfitapplication.datasource.database.jpa.MembershipRepository
import com.crossfit.crossfitapplication.datasource.database.models.entites.economy.Membership
import com.crossfit.crossfitapplication.datasource.error.DataSourceError
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import org.springframework.stereotype.Service

@Service
class MembershipDataSource(private val membershipRepository: MembershipRepository) {

    fun createMembership(membership: Membership): Result<Membership, DataSourceError> {
        return com.github.michaelbull.result.runCatching {
            membershipRepository.save(membership)
        }.mapError { error ->
            error.toDataSourceError()
        }
    }
}
