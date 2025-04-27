package com.crossfit.crossfitapplication.service.models

import java.math.BigDecimal
import java.time.OffsetDateTime

data class MembershipDto(
    val id: Long? = null,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val isPaid: Boolean,
    val price: BigDecimal,
    val memberId: Long? = null,
)
