package com.crossfit.crossfitapplication.service.models

import java.time.OffsetDateTime

data class PhysicalParameterDto(
    val id: Long? = null,
    val date: OffsetDateTime? = null,
    val weight: Float? = null,
    val height: Float? = null,
    val skinfolds: SkinfoldsDto? = null,
    val measurements: MeasurementsDto? = null,
    val memberKeycloakId: String,
)
