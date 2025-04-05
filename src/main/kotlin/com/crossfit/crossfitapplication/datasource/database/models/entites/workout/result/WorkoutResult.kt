package com.crossfit.crossfitapplication.datasource.database.models.entites.workout.result

import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import com.crossfit.crossfitapplication.datasource.database.models.entites.workout.exercise.ExerciseResult
import com.crossfit.crossfitapplication.datasource.database.models.entites.workout.plan.Workout
import jakarta.persistence.*
import java.time.LocalTime

@Entity
class WorkoutResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @ManyToOne
    lateinit var member: Member

    @ManyToOne
    var workout: Workout? = null
    var sessionTime: LocalTime? = null
    var roundsCompleted: Int = 0
    var additionalRepetitions: Int = 0
    var totalTime: Float = 0.0f

    @OneToMany(mappedBy = "workoutResult", cascade = [CascadeType.ALL], orphanRemoval = true)
    var results: MutableList<ExerciseResult> = mutableListOf()
}
