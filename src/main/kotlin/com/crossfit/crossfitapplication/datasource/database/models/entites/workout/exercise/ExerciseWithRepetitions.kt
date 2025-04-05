package com.crossfit.crossfitapplication.datasource.database.models.entites.workout.exercise

import jakarta.persistence.Embeddable
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Embeddable
class ExerciseWithRepetitions {
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    val exercise: Exercise? = null
    val defaultRepetitions: Int? = null
}
