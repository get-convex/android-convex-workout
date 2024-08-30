package dev.convex.workouttracker.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.convex.workouttracker.WorkoutApplication
import dev.convex.workouttracker.core.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.math.absoluteValue

class OverviewViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _selectedWeek = mutableStateOf(defaultStartDate)

    /**
     * The start date of the calendar week that should be shown in the UI.
     *
     * Update by calling [selectWeek].
     */
    val selectedWeek: State<LocalDate> = _selectedWeek

    /**
     * Changes the [selectedWeek] shown in the UI.
     *
     * The new value should be +/- 7 days from the prior [selectedWeek].
     */
    fun selectWeek(startDate: LocalDate) {
        assert(_selectedWeek.value.minus(startDate).days.absoluteValue == 7)
        _selectedWeek.value = startDate
    }

    /**
     * A state flow containing a map of dates to a workout for that date (if any).
     */
    // TODO update to support multiple workouts for a date.
    val workouts = flow {
        emitAll(repository.subscribeToWorkouts())
    }.transform { result ->
        result.onSuccess { value ->
            emit(value)
        }
    }
        .transform { workouts ->
            emit(workouts.associateBy({
                LocalDate.parse(it.date)
            }, { workout ->
                workout
            }))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), mapOf())


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val workoutRepository = (this[APPLICATION_KEY] as WorkoutApplication).repository
                OverviewViewModel(
                    repository = workoutRepository,
                )
            }
        }

        val defaultStartDate get() = Clock.System.todayIn(TimeZone.currentSystemDefault()).let {it.minus(
            DatePeriod(days = it.dayOfWeek.ordinal)
        )}
    }
}