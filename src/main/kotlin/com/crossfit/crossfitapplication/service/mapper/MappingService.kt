package com.crossfit.crossfitapplication.service.mapper

import com.crossfit.crossfitapplication.application.response.MemberResponse
import com.crossfit.crossfitapplication.datasource.database.models.entites.economy.Membership
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import com.crossfit.crossfitapplication.service.models.MembershipDto
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class MappingService {
    fun memberEntityToDto(member: Member): MemberResponse {
        return MemberResponse(
            firstName = member.firstName ?: "",
            lastName = member.lastName ?: "",
            email = member.lastName ?: "",
            phoneNumber = member.phoneNumber ?: "",
            dateOfBirth = member.dateOfBirth ?: OffsetDateTime.MIN,
            joinDate = member.joinDate ?: OffsetDateTime.MIN,
//            memberships = member.memberships.map { it.toDto() },
//            workouts = emptyList()
        )
    }

    fun memberDtoToEntity(memberResponse: MemberResponse): Member {
        return Member(
            firstName = memberResponse.firstName,
            lastName = memberResponse.lastName,
            dateOfBirth = memberResponse.dateOfBirth,
            phoneNumber = memberResponse.phoneNumber,
            joinDate = memberResponse.joinDate,
            email = memberResponse.email,
//            memberships = memberDto.memberships.map { it.toEntity() }.toMutableList()
        )
    }

    fun membershipEntityToDto(membership: Membership): MembershipDto {
        return MembershipDto(
            startDate = membership.startDate,
            endDate = membership.endDate,
            isPaid = membership.isPaid,
            price = membership.price,
        )
    }

    fun membershipDtoToEntity(membershipDto: MembershipDto): Membership {
        return Membership(
            startDate = membershipDto.startDate,
            endDate = membershipDto.endDate,
            isPaid = membershipDto.isPaid,
            price = membershipDto.price,
        )
    }
}
