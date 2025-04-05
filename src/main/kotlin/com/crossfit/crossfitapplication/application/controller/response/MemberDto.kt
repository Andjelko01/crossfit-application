package com.crossfit.crossfitapplication.application.controller.response

import java.time.OffsetDateTime

data class MemberDto(
    val username: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val dateOfBirth: OffsetDateTime,
    val joinDate: OffsetDateTime,
    val workouts: List<WorkoutDto> = emptyList(),
    val memberships: List<MembershipDto> = emptyList(),
)
