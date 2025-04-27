package com.crossfit.crossfitapplication.application.response

import com.crossfit.crossfitapplication.service.models.MembershipDto
import org.bouncycastle.asn1.ocsp.ResponseData
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