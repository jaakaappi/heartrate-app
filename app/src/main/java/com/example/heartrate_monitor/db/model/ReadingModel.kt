package com.example.heartrate_monitor.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "reading",
    foreignKeys = [ForeignKey(
        ExerciseModel::class,
        parentColumns = ["id"],
        childColumns = ["exercise_id"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class ReadingModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "exercise_id", index = true) val exerciseId: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long?,
    @ColumnInfo(name = "reading") val reading: Int?
)