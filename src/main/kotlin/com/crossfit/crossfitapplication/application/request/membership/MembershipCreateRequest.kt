package com.crossfit.crossfitapplication.application.request.membership

import java.math.BigDecimal
import java.time.OffsetDateTime

data class MembershipCreateRequest(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val isPaid: Boolean,
    val price: BigDecimal,
)
