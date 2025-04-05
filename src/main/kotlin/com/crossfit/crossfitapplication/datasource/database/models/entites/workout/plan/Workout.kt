package com.crossfit.crossfitapplication.datasource.database.models.entites.workout.plan

import com.crossfit.crossfitapplication.datasource.database.models.entites.workout.exercise.ExerciseWithRepetitions
import com.crossfit.crossfitapplication.datasource.database.models.enums.WorkoutType
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val name: String? = null
    val date: OffsetDateTime? = null

    @Enumerated(EnumType.STRING)
    val type: WorkoutType? = null

    @ElementCollection
    @CollectionTable(name = "exercise_repetition_map", joinColumns = [JoinColumn(name = "workout_id")])
    val exercisesWithRepetitions: List<ExerciseWithRepetitions> = mutableListOf()
}
