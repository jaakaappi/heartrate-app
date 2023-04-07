package com.example.heartrate_monitor.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.heartrate_monitor.db.model.ExerciseModel
import dagger.Provides
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert
    suspend fun add(exercise: ExerciseModel)

    @Delete
    suspend fun delete(exercise: ExerciseModel)

    @Query("DELETE FROM exercise")
    suspend fun deleteAll()


    @Query("SELECT * from exercise")
    fun getAll(): Flow<List<ExerciseModel>>

    @Query("SELECT * from exercise where id = :id")
    suspend fun getById(id: Int): ExerciseModel
}