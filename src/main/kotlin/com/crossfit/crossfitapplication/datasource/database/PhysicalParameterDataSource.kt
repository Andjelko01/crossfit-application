package com.crossfit.crossfitapplication.datasource.database

import com.crossfit.crossfitapplication.datasource.database.common.toDataSourceError
import com.crossfit.crossfitapplication.datasource.database.jpa.MeasurementsRepository
import com.crossfit.crossfitapplication.datasource.database.jpa.PhysicalParameterRepository
import com.crossfit.crossfitapplication.datasource.database.jpa.SkinfoldsRepository
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.measurements.PhysicalParameter
import com.crossfit.crossfitapplication.datasource.error.DataSourceError
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import org.springframework.stereotype.Service

@Service
class PhysicalParameterDataSource(
    private val physicalParameterRepository: PhysicalParameterRepository,
    private val measurementsRepository: MeasurementsRepository,
    private val skinfoldsRepository: SkinfoldsRepository
) {

    fun createPhysicalParameter(physicalParameter: PhysicalParameter): Result<PhysicalParameter, DataSourceError> {
        return com.github.michaelbull.result.runCatching {
            physicalParameterRepository.save(physicalParameter)
        }.mapError { error ->
            error.toDataSourceError()
        }
    }

}