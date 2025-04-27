package com.crossfit.crossfitapplication.application.request.member

import java.time.OffsetDateTime

data class MemberCreateRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String?,
    val dateOfBirth: OffsetDateTime?,
    val joinDate: OffsetDateTime?,
    val password: String,
)
