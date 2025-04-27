package com.crossfit.crossfitapplication.application.response

import java.time.OffsetDateTime

data class MemberResponse(
    val username: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val dateOfBirth: OffsetDateTime,
    val joinDate: OffsetDateTime,
    val workouts: List<WorkoutDto> = emptyList(),
    val memberships: List<MembershipResponse> = emptyList(),
)
