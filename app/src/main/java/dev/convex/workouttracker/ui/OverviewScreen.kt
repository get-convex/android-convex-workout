package dev.convex.workouttracker.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.convex.workouttracker.models.Workout
import dev.convex.workouttracker.ui.theme.WorkoutTrackerTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel,
    onClickAddWorkout: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    OverviewContent(
        onClickAddWorkout,
        viewModel::selectWeek,
        uiState,
    )
}

@Composable
fun OverviewContent(
    onClickAddWorkout: () -> Unit = {},
    onWeekSelected: (startDate: LocalDate) -> Unit = {},
    uiState: UiState = UiState(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Workout Tracker",
            style = MaterialTheme.typography.headlineLarge
        )
        Week(
            onWeekSelected = onWeekSelected,
            selectedWeek = uiState.selectedWeek,
            workoutData = uiState.allWorkouts
        )
        WorkoutFeed(workouts = uiState.workoutsForWeek)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(onClick = onClickAddWorkout) {
                Text("Add Workout")
            }
        }

    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 680)
@Composable
fun OverviewContentPreview() {
    WorkoutTrackerTheme {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        var selectedWeek by remember {
            mutableStateOf(UiState.defaultStartDate)
        }
        OverviewContent(
            onWeekSelected = { selectedWeek = it },
            uiState = UiState(
                selectedWeek = selectedWeek,
                allWorkouts = mapOf(
                    today.minus(DatePeriod(days = 5)) to listOf(
                        Workout(
                            "junk data",
                            Workout.Activity.Running
                        )
                    ),
                    today to listOf(
                        Workout(
                            "junk data",
                            Workout.Activity.Swimming
                        )
                    )
                ),
                workoutsForWeek = listOf(
                    Workout(
                        "junk data",
                        Workout.Activity.Swimming
                    ),
                    Workout(
                        "junk data",
                        Workout.Activity.Running
                    ),
                    Workout(
                        "junk data",
                        Workout.Activity.Lifting
                    ),
                    Workout(
                        "junk data",
                        Workout.Activity.Walking
                    ),
                )
            )
        )
    }
}

private val DAY_SIZE = 24.dp

@Composable
fun Week(
    onWeekSelected: (startOfWeek: LocalDate) -> Unit = {},
    selectedWeek: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    workoutData: Map<LocalDate, List<Workout>> = mapOf()
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onWeekSelected(selectedWeek.minus(DatePeriod(days = 7)))
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = ""
                )
            }
            Text(
                text = "Week of ${
                    selectedWeek.month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
                } ${selectedWeek.dayOfMonth}",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                onWeekSelected(selectedWeek.plus(DatePeriod(days = 7)))
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = ""
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            DayHeader(text = "M")
            DayHeader(text = "T")
            DayHeader(text = "W")
            DayHeader(text = "T")
            DayHeader(text = "F")
            DayHeader(text = "S")
            DayHeader(text = "S")
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            for (offset in 0..6) {
                Dot(filled = workoutData.containsKey(selectedWeek.plus(DatePeriod(days = offset))))
            }
        }
    }
}

@Composable
fun DayHeader(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        modifier = Modifier.size(DAY_SIZE)
    )
}


@Composable
fun Dot(
    modifier: Modifier = Modifier,
    filled: Boolean = false,
) {
    val color = if (filled) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.inversePrimary
    }
    Canvas(modifier.size(DAY_SIZE)) {
        if (filled) {
            drawCircle(color, radius = DAY_SIZE.toPx() / 3)
        } else {
            drawCircle(color, radius = DAY_SIZE.toPx() / 6)
        }

    }
}

@Composable
fun WorkoutFeed(workouts: List<Workout> = listOf()) {
    LazyColumn {
        items(workouts) {
            Text(text = it.activity.toString())
        }
    }
}