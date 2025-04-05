package com.crossfit.crossfitapplication.datasource.database.models.entites.workout.plan

import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import com.crossfit.crossfitapplication.datasource.database.models.entites.workout.exercise.CustomizedRepetition
import jakarta.persistence.*
import java.util.*

@Entity
class CustomizedWorkoutPlan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,
    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    val workout: Workout,
    @OneToMany(mappedBy = "customizedWorkoutPlan", cascade = [CascadeType.ALL], orphanRemoval = true)
    val customizedRepetitions: MutableList<CustomizedRepetition> = mutableListOf(),
)
