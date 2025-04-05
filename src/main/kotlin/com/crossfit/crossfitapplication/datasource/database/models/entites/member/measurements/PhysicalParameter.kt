package com.crossfit.crossfitapplication.datasource.database.models.entites.member.measurements

import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
class PhysicalParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val date: OffsetDateTime? = null
    val weight: Float? = null
    val height: Float? = null

    @OneToOne(cascade = [CascadeType.ALL])
    val skinfolds: Skinfolds? = null

    @OneToOne(cascade = [CascadeType.ALL])
    val measurements: Measurements? = null

    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member? = null
}
