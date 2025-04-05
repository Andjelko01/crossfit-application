package com.crossfit.crossfitapplication.datasource.database.models.entites.workout.exercise

import com.crossfit.crossfitapplication.datasource.database.models.entites.workout.result.WorkoutResult
import jakarta.persistence.*
import java.util.*

@Entity
class ExerciseResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne
    val exercise: Exercise? = null
    val weight: Double? = null
    val repetitions: Int? = null
    val height: Double? = null

    @ManyToOne
    @JoinColumn(name = "workout_result_id", nullable = false)
    var workoutResult: WorkoutResult? = null
}
