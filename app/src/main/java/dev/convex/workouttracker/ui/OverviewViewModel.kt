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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
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
        assert(_selectedWeek.value.minus(startDate).days.absoluteValue == 7)
        _selectedWeek.value = startDate
    }

    /**
     * A state flow containing a map of dates to workouts for that date (if any).
     */
    val uiState = flow {
        emitAll(repository.subscribeToWorkouts())
    }.transform { result ->
        result.onSuccess { value ->
            emit(value)
        }
    }
        .transform<List<Workout>, Map<LocalDate, List<Workout>>> { workouts ->
            val workoutMap = mutableMapOf<LocalDate, MutableList<Workout>>()
            workouts.forEach {
                workoutMap.getOrPut(LocalDate.parse(it.date)) { mutableListOf() }.add(it)
            }
            emit(workoutMap)
        }.combineTransform(_selectedWeek) { workouts, selectedWeek ->
            // TODO finish creating a UiState instance to emit and update the app to handle.
            val workoutsForWeek = mutableListOf<Workout>()
            for (offset in 0..6) {
                workoutsForWeek.addAll(
                    workouts.getOrDefault(
                        selectedWeek.plus(
                            offset,
                            DateTimeUnit.DAY
                        ), listOf()
                    )
                )
            }
            emit(
                UiState(
                    selectedWeek = selectedWeek,
                    allWorkouts = workouts,
                    workoutsForWeek = workoutsForWeek
                )
            )
        }
        .viewModelScopedStateIn(initialValue = UiState())

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
    val selectedWeek: LocalDate = defaultStartDate,
    val allWorkouts: Map<LocalDate, List<Workout>> = mapOf(),
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