package com.crossfit.crossfitapplication.application.request.measurements.create

data class SkinfoldsCreateRequest(
    val biceps: Float?,
    val triceps: Float?,
    val shoulderBlade: Float?,
    val back: Float?,
    val stomach: Float?,
    val glutes: Float?,
    val frontThigh: Float?,
    val rearThigh: Float?
)
