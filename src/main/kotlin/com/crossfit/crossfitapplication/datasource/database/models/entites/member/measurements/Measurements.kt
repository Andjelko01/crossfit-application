package com.crossfit.crossfitapplication.datasource.database.models.entites.member.measurements

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
class Measurements(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val upperArm: Float? = null,
    val chest: Float? = null,
    val stomach: Float? = null,
    val hips: Float? = null,
    val thigh: Float? = null
)
