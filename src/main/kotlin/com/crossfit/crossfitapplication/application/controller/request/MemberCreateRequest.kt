package com.crossfit.crossfitapplication.application.controller.request

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
