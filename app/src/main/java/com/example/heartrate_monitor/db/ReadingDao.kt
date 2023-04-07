package com.example.heartrate_monitor.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.heartrate_monitor.db.model.ReadingModel

@Dao
interface ReadingDao {

    @Insert
    fun add(reading: ReadingModel)

    @Delete
    fun delete(reading: ReadingModel)

    @Query("SELECT * FROM reading WHERE exercise_id = :exercise_id")
    fun getByExerciseId(exercise_id: Int): List<ReadingModel>
}