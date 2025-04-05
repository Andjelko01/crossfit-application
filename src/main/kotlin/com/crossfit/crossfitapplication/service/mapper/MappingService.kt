package com.crossfit.crossfitapplication.service.mapper

import com.crossfit.crossfitapplication.application.controller.response.MemberDto
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class MappingService {
    fun memberEntityToDto(member: Member): MemberDto {
        return MemberDto(
            firstName = member.firstName ?: "",
            lastName = member.lastName ?: "",
            email = member.lastName ?: "",
            phoneNumber = member.phoneNumber ?: "",
            dateOfBirth = member.dateOfBirth ?: OffsetDateTime.MIN,
            joinDate = member.joinDate ?: OffsetDateTime.MIN,
//            memberships = member.memberships.map { it.toDto() },
//            workouts = emptyList() // Dodajte logiku za Workouts ako je potrebno
        )
    }

    fun memberDtoToEntity(memberDto: MemberDto): Member {
        return Member(
            firstName = memberDto.firstName,
            lastName = memberDto.lastName,
            dateOfBirth = memberDto.dateOfBirth,
            phoneNumber = memberDto.phoneNumber,
            joinDate = memberDto.joinDate,
            email = memberDto.email,
//            memberships = memberDto.memberships.map { it.toEntity() }.toMutableList()
        )
    }
}
