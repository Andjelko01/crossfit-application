package com.crossfit.crossfitapplication.datasource.database.models.entites.economy

import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
class Membership(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val isPaid: Boolean = false,
    val price: BigDecimal,
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null,
)
