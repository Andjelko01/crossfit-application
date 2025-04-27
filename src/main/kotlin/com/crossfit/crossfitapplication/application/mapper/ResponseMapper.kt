package com.crossfit.crossfitapplication.application.mapper

import com.crossfit.crossfitapplication.application.response.MembershipResponse
import com.crossfit.crossfitapplication.service.models.MembershipDto
import org.springframework.stereotype.Component

@Component
class ResponseMapper {

    fun membershipDtoToMembershipResponse(membershipDto: MembershipDto): MembershipResponse {
        return MembershipResponse(
            id = membershipDto.id,
            startDate = membershipDto.startDate,
            endDate = membershipDto.endDate,
            isPaid = membershipDto.isPaid,
            price = membershipDto.price,
        )
    }
}