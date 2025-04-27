package com.crossfit.crossfitapplication.application.response

import java.math.BigDecimal
import java.time.OffsetDateTime

data class MembershipResponse(
    val id: Long?,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val isPaid: Boolean,
    val price: BigDecimal,
)
