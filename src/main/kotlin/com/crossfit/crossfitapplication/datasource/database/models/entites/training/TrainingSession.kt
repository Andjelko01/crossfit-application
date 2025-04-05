package com.crossfit.crossfitapplication.datasource.database.models.entites.training

import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val startTime: LocalDateTime? = null
    val locked: Boolean = false

    @OneToMany(fetch = FetchType.LAZY)
    val users: MutableList<Member> = mutableListOf()
}
