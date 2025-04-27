package com.crossfit.crossfitapplication.datasource.database

import com.crossfit.crossfitapplication.datasource.database.common.toDataSourceError
import com.crossfit.crossfitapplication.datasource.database.jpa.MemberRepository
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import com.crossfit.crossfitapplication.datasource.error.DataSourceError
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import org.springframework.stereotype.Service

@Service
class MemberDataSource(private val memberRepository: MemberRepository) {

    fun createMember(member: Member): Result<Member, DataSourceError> {
        return com.github.michaelbull.result.runCatching {
            memberRepository.save(member)
        }.mapError { error ->
            error.toDataSourceError()
        }
    }

    fun deleteMember(keycloakId: String): Result<Unit, DataSourceError> {
        return com.github.michaelbull.result.runCatching {
            memberRepository.deleteMemberByKeycloakId(keycloakId)
        }.mapError { error ->
            error.toDataSourceError()
        }
    }

    fun getMemberByKeycloakId(keycloakId: String): Result<Member, DataSourceError> {
        return com.github.michaelbull.result.runCatching {
            memberRepository.getMemberByKeycloakId(keycloakId)
        }.mapError { error ->
            error.toDataSourceError()
        }
    }
}
