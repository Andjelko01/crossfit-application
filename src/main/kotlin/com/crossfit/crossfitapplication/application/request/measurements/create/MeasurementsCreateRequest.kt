package com.crossfit.crossfitapplication.application.request.measurements.create

data class MeasurementsCreateRequest(
    val upperArm: Float?,
    val chest: Float?,
    val stomach: Float?,
    val hips: Float?,
    val thigh: Float?
)
