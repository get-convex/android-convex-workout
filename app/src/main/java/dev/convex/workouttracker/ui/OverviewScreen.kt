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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.convex.workouttracker.ui.theme.WorkoutTrackerTheme

@Composable
fun OverviewScreen(
    onClickAddWorkout: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Workout Tracker",
            style = MaterialTheme.typography.headlineLarge
        )
        Week()
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
        OverviewScreen()
    }
}

private val DAY_SIZE = 24.dp

@Composable
fun Week() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = ""
                )
            }
            Text(
                text = "Week of Aug 26",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /*TODO*/ }) {
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
            Dot()
            Dot(true)
            Dot()
            Dot()
            Dot()
            Dot()
            Dot()
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