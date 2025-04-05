package com.crossfit.crossfitapplication.datasource.database.models.entites.workout.exercise

import com.crossfit.crossfitapplication.datasource.database.models.entites.workout.plan.CustomizedWorkoutPlan
import jakarta.persistence.*

@Entity
class CustomizedRepetition(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    val customizedWorkoutPlan: CustomizedWorkoutPlan,
    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    val exercise: Exercise,
    @Column(name = "weight")
    val weight: Float?,
    @Column(name = "height")
    val height: Float?,
)
