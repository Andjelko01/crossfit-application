package com.crossfit.crossfitapplication.service

import com.crossfit.crossfitapplication.datasource.database.MemberDataSource
import com.crossfit.crossfitapplication.datasource.database.MembershipDataSource
import com.crossfit.crossfitapplication.service.error.ServiceError
import com.crossfit.crossfitapplication.service.mapper.MappingService
import com.crossfit.crossfitapplication.service.mapper.toServiceError
import com.crossfit.crossfitapplication.service.models.MembershipDto
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import org.springframework.stereotype.Service

@Service
class MembershipService(private val membershipDataSource: MembershipDataSource, private val mappingService: MappingService, private val memberDataSource: MemberDataSource) {

    fun createMembership(membershipDto: MembershipDto, memberKeycloakId: String): Result<MembershipDto, ServiceError> {

        val membership = mappingService.membershipDtoToEntity(membershipDto)

        return memberDataSource.getMemberByKeycloakId(memberKeycloakId)
            .mapError { it.toServiceError() }
            .andThen { member ->
                membership.member = member
                membershipDataSource.createMembership(membership)
                    .mapError { it.toServiceError() }
            }
            .andThen {
                Ok(mappingService.membershipEntityToDto(it))
            }
    }

}