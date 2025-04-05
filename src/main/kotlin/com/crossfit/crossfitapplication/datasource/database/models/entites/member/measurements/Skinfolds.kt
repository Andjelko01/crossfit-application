package com.crossfit.crossfitapplication.datasource.database.models.entites.member.measurements

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
class Skinfolds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val biceps: Float? = null
    val triceps: Float? = null
    val scapula: Float? = null
    val back: Float? = null
    val stomach: Float? = null
    val glutes: Float? = null
    val frontThigh: Float? = null
    val rearThigh: Float? = null
}
