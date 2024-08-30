package dev.convex.workouttracker.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.convex.workouttracker.WorkoutApplication
import dev.convex.workouttracker.core.WorkoutRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: WorkoutRepository) : ViewModel() {
    /**
     * Holds the current authentication state of the application.
     */
    val authState get() = repository.authState

    /**
     * Triggers a sign-in flow which will update the [authState].
     */
    fun signIn(context: Context) {
        viewModelScope.launch {
            repository.signIn(context)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val workoutRepository = (this[APPLICATION_KEY] as WorkoutApplication).repository
                AuthViewModel(
                    repository = workoutRepository,
                )
            }
        }
    }
}