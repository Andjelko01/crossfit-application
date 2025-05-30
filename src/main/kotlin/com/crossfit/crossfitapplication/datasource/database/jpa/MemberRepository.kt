package com.crossfit.crossfitapplication.datasource.database.jpa

import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun deleteMemberByKeycloakId(keycloakId: String)
    fun getMemberByKeycloakId(keycloakId: String): Member
}
