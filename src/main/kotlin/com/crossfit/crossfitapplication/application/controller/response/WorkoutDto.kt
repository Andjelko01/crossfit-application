package com.crossfit.crossfitapplication.application.controller.response

import java.time.OffsetDateTime
import java.util.UUID

data class WorkoutDto(
    val id: UUID,
    val name: String,
    val date: OffsetDateTime,
    val type: String,
//    val exercises: List<ExerciseDto> = emptyList()
)
