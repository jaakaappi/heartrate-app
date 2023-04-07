package com.example.heartrate_monitor.db

import com.example.heartrate_monitor.db.model.ExerciseModel
import kotlinx.coroutines.flow.Flow

class HeartrateRepository(private val exerciseDao: ExerciseDao) {
    fun getAllExercises(): Flow<List<ExerciseModel>> = exerciseDao.getAll()

    suspend fun addExercise(exerciseModel: ExerciseModel) = exerciseDao.add(exerciseModel)
    suspend fun deleteAllExercises() = exerciseDao.deleteAll()
}