package com.crossfit.crossfitapplication.service

import com.crossfit.crossfitapplication.datasource.database.MemberDataSource
import com.crossfit.crossfitapplication.datasource.database.PhysicalParameterDataSource
import com.crossfit.crossfitapplication.service.error.ServiceError
import com.crossfit.crossfitapplication.service.mapper.PhysicalParameterServiceMapper
import com.crossfit.crossfitapplication.service.mapper.toServiceError
import com.crossfit.crossfitapplication.service.models.PhysicalParameterDto
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapError
import org.springframework.stereotype.Service

@Service
class PhysicalParameterService(
    private val physicalParameterDataSource: PhysicalParameterDataSource,
    private val physicalParameterServiceMapper: PhysicalParameterServiceMapper,
    private val memberDataSource: MemberDataSource,
) {

    fun createPhysicalParameter(physicalParameterDto: PhysicalParameterDto): Result<PhysicalParameterDto, ServiceError> {
        return memberDataSource.getMemberByKeycloakId(physicalParameterDto.memberKeycloakId)
            .mapError { it.toServiceError() }
            .andThen { member ->
                val physicalParameter = physicalParameterServiceMapper.toPhysicalParameterEntity(physicalParameterDto, member)
                physicalParameter.member = member
                physicalParameterDataSource.createPhysicalParameter(physicalParameter)
                    .mapError { it.toServiceError() }
            }
            .andThen {
                Ok(physicalParameterServiceMapper.toPhysicalParameterDto(it))
            }
    }
}
