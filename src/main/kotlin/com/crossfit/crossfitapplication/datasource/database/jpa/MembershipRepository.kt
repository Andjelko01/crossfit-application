package com.crossfit.crossfitapplication.datasource.database.jpa

import com.crossfit.crossfitapplication.datasource.database.models.entites.economy.Membership
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MembershipRepository : JpaRepository<Membership, Long> {
}