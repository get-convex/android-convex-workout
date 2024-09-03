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
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: WorkoutRepository) : ViewModel() {
    /**
     * Holds the current authentication state of the application.
     */
    // We drop the first value from the flow here b/c it's for the unauthenticated state. The app
    // triggers an automatic sign in on launch, and this prevents the unauthenticated state from
    // flashing in on launch.
    // TODO update the auth integration to allow a custom start state?
    val authState get() = repository.authState.drop(1)

    /**
     * Triggers a sign-in flow which will update the [authState].
     */
    fun signIn(context: Context) {
        viewModelScope.launch {
            repository.signIn(context)
        }
    }

    /**
     * Attempts to sign in with previously cached credentials.
     *
     * If this doesn't move the [authState] to authenticated, interactive sign in via [signIn] will
     * be required.
     */
    fun signInAutomatically() {
        viewModelScope.launch {
            repository.signInWithCachedCredentials()
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