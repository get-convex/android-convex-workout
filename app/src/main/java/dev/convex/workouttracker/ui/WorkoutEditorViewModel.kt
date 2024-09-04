package dev.convex.workouttracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.convex.workouttracker.WorkoutApplication
import dev.convex.workouttracker.core.WorkoutRepository
import dev.convex.workouttracker.models.Workout
import kotlinx.coroutines.launch

class WorkoutEditorViewModel(private val repository: WorkoutRepository) : ViewModel() {
    /**
     * Stores the given [workout].
     *
     * It can be later retrieved via the [OverviewViewModel].
     */
    fun storeWorkout(workout: Workout, storeComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.storeWorkout(workout)
            // If we reached here then it was successfully saved on the backend
            storeComplete()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val workoutRepository = (this[APPLICATION_KEY] as WorkoutApplication).repository
                WorkoutEditorViewModel(
                    repository = workoutRepository,
                )
            }
        }
    }
}