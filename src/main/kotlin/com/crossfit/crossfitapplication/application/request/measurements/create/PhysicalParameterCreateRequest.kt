package com.crossfit.crossfitapplication.application.request.measurements.create

import java.time.OffsetDateTime

data class PhysicalParameterCreateRequest(
    val date: OffsetDateTime?,
    val weight: Float?,
    val height: Float?,
    val skinfolds: SkinfoldsCreateRequest?,
    val measurements: MeasurementsCreateRequest?,
    val memberKeycloakId: String?,
)
