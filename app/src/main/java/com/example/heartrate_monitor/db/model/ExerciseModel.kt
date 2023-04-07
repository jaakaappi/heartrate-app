package com.example.heartrate_monitor.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
data class ExerciseModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "start_timestamp") val startTimestamp: Long?,
    @ColumnInfo(name = "end_timestamp") val endTimestamp: Long?,
)