package com.crossfit.crossfitapplication.application.request.membership

import java.time.OffsetDateTime
import java.math.BigDecimal

data class MembershipCreateRequest(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val isPaid: Boolean,
    val price: BigDecimal
)