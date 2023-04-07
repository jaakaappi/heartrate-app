package com.example.heartrate_monitor.db

import androidx.room.RoomDatabase
import com.example.heartrate_monitor.db.model.ExerciseModel
import com.example.heartrate_monitor.db.model.ReadingModel
import javax.inject.Singleton

@Singleton
@androidx.room.Database(version = 1, entities = [ExerciseModel::class, ReadingModel::class])
abstract class HeartrateDatabase : RoomDatabase() {
    abstract fun getExerciseDao(): ExerciseDao
    abstract fun getReadingDao(): ReadingDao
}