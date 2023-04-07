package com.example.heartrate_monitor.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heartrate_monitor.db.model.ExerciseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewmodel @Inject constructor(
    private val repository: HeartrateRepository
) : ViewModel() {
    val exercises = repository.getAllExercises()

    fun addExercise() = viewModelScope.launch {
        repository.addExercise(ExerciseModel(0, 0, 0))
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAllExercises()
    }
}