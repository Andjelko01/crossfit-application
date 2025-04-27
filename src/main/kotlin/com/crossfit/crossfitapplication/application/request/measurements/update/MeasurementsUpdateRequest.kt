package com.crossfit.crossfitapplication.application.request.measurements.update

data class MeasurementsUpdateRequest(
    val id: Long?,
    val upperArm: Float?,
    val chest: Float?,
    val stomach: Float?,
    val hips: Float?,
    val thigh: Float?,
)
