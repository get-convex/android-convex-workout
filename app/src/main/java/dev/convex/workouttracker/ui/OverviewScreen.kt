package dev.convex.workouttracker.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    onClickAddWorkout: () -> Unit = {},
    onWeekSelected: (startOfWeek: LocalDate) -> Unit = {},
    workoutData: Map<LocalDate, Workout> = mapOf()
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    var selectedStartOfWeek by remember {
        mutableStateOf(today.minus(DatePeriod(days = today.dayOfWeek.ordinal)))
    }
    LaunchedEffect("creation") {
        onWeekSelected(selectedStartOfWeek)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Workout Tracker",
            style = MaterialTheme.typography.headlineLarge
        )
        Week(onWeekSelected = { week ->
            selectedStartOfWeek = week
            onWeekSelected(week)
        }, selectedWeek = selectedStartOfWeek, workoutData = workoutData)
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
fun OverviewScreenPreview() {
    WorkoutTrackerTheme {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        OverviewScreen(
            workoutData = mapOf(
                today.minus(DatePeriod(days = 5)) to Workout(
                    "junk data",
                    Workout.Activity.Running
                ),
                today to Workout(
                    "junk data",
                    Workout.Activity.Swimming
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
    workoutData: Map<LocalDate, Workout> = mapOf()
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
                Dot(workoutData.containsKey(selectedWeek.plus(DatePeriod(days = offset))))
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
    filled: Boolean = false,
    modifier: Modifier = Modifier
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