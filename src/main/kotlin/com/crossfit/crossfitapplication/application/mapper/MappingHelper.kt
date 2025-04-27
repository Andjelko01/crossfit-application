package com.crossfit.crossfitapplication.application.mapper

import com.crossfit.crossfitapplication.application.request.membership.MembershipCreateRequest
import com.crossfit.crossfitapplication.service.models.MembershipDto

fun MembershipCreateRequest.toMembershipDto(): MembershipDto {
    return MembershipDto(
        startDate = this.startDate,
        endDate = this.endDate,
        isPaid = this.isPaid,
        price = this.price,
    )
}