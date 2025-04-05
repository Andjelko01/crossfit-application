package com.crossfit.crossfitapplication.datasource.database.models.entites.workout.exercise

import jakarta.persistence.Embeddable

@Embeddable
data class ExerciseAttributes(
    val weight: Float?,
    val height: Float?,
)
