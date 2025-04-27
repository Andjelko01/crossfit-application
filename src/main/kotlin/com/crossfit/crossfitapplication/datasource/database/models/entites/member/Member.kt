package com.crossfit.crossfitapplication.datasource.database.models.entites.member

import com.crossfit.crossfitapplication.datasource.database.models.entites.economy.Membership
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.measurements.PhysicalParameter
import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: OffsetDateTime? = null,
    val phoneNumber: String? = null,
    val joinDate: OffsetDateTime? = null,
    val email: String? = null,
    val username: String? = null,
    @Column(unique = true, nullable = false)
    val keycloakId: String? = null,
    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val memberships: MutableList<Membership> = mutableListOf(),
    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL])
    val physicalParameterHistory: MutableList<PhysicalParameter>? = null,
)
