package com.crossfit.crossfitapplication.application.request.measurements.update

import java.time.OffsetDateTime

data class PhysicalParameterUpdateRequest(
    val date: OffsetDateTime?,
    val weight: Float?,
    val height: Float?,
    val skinfolds: SkinfoldsUpdateRequest?,
    val measurements: MeasurementsUpdateRequest?,
)
