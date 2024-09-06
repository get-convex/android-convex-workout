package dev.convex.workouttracker.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.convex.workouttracker.WorkoutApplication
import dev.convex.workouttracker.core.WorkoutRepository
import dev.convex.workouttracker.models.Workout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.math.absoluteValue

class OverviewViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _selectedWeek = MutableStateFlow(UiState.defaultStartDate)

    /**
     * Changes the selected week shown in the UI.
     *
     * The new value should be +/- 7 days from the prior selected week.
     */
    fun selectWeek(startDate: LocalDate) {
        Log.d("Selected Week change ", startDate.toString())
        assert(_selectedWeek.value.minus(startDate).days.absoluteValue == 7)
        _selectedWeek.value = startDate
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = _selectedWeek.flatMapLatest { selectedWeek ->
        repository.subscribeToWorkoutsInRange(
            selectedWeek.toString(),
            selectedWeek.plus(DatePeriod(days = 6)).toString()
        )
    }.transform { result ->
        result.onSuccess { workouts ->
            Log.d("Flow", "Got workouts $workouts")
            emit(workouts)
        }
        result.onFailure {
            Log.d("Flow", "Failed to get workouts")
            emit(listOf<Workout>())
        }
    }.transform { workouts ->
        emit(
            UiState(
                loading = false,
                selectedWeek = _selectedWeek.value, // TODO this is wrong I should be getting from the flow
                workoutsForWeek = workouts
            )
        )
    }.viewModelScopedStateIn(initialValue = UiState())

    /**
     * Converts this [Flow] to a `StateFlow` scoped to the lifetime of the associated [ViewModel].
     *
     * When the [ViewModel] goes out of scope, the [Flow] will be canceled.
     */
    private fun <T> Flow<T>.viewModelScopedStateIn(initialValue: T) =
        stateIn(viewModelScope, SharingStarted.Lazily, initialValue)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val workoutRepository = (this[APPLICATION_KEY] as WorkoutApplication).repository
                OverviewViewModel(
                    repository = workoutRepository,
                )
            }
        }
    }
}

data class UiState(
    val loading: Boolean = true,
    val selectedWeek: LocalDate = defaultStartDate,
    val workoutsForWeek: List<Workout> = listOf()
) {
    companion object {
        val defaultStartDate
            get() = Clock.System.todayIn(TimeZone.currentSystemDefault()).let {
                it.minus(
                    DatePeriod(days = it.dayOfWeek.ordinal)
                )
            }
    }
}