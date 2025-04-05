package com.crossfit.crossfitapplication.application.controller.response

import java.time.OffsetDateTime
import java.util.UUID

data class MembershipDto(
    val id: UUID,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val paid: Boolean,
)
