package dev.convex.workouttracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.convex.workouttracker.models.Workout
import dev.convex.workouttracker.ui.theme.WorkoutTrackerTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun WorkoutEditorScreen(
    viewModel: WorkoutEditorViewModel,
    workoutSaved: () -> Unit = {}
) {
    WorkoutEditorContent(onSave = { workout ->
        viewModel.storeWorkout(workout) {
            workoutSaved()
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutEditorContent(onSave: (workout: Workout) -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val datePickerState = rememberDatePickerState()
        var selectedActivity by remember { mutableStateOf<Workout.Activity?>(null) }
        var duration by remember { mutableStateOf(TextFieldValue()) }
        DatePicker(datePickerState = datePickerState)
        ActivityPicker(selectedActivity = selectedActivity) { activity ->
            selectedActivity = activity
        }
        TextField(
            // Duration picker
            value = duration,
            label = { Text("Duration") },
            onValueChange = {
                if (it.text.all(Char::isDigit)) {
                    duration = it
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            enabled = selectedActivity != null && datePickerState.selectedDateMillis != null,
            onClick = {
                onSave(
                    Workout(
                        date = convertMillisToDate(datePickerState.selectedDateMillis!!),
                        activity = selectedActivity!!,
                        duration = duration.text.toInt()
                    )
                )
            }) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun WorkoutEditorContentPreview() {
    WorkoutTrackerTheme {
        WorkoutEditorContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityPicker(
    selectedActivity: Workout.Activity?,
    onActivitySelected: (activity: Workout.Activity) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            readOnly = true,
            value = selectedActivity?.toString() ?: "",
            onValueChange = { },
            label = { Text("Activity") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Workout.Activity.options.forEach { selectedActivity ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectedActivity.toString())
                    },
                    onClick = {
                        onActivitySelected(selectedActivity)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(datePickerState: DatePickerState = rememberDatePickerState()) {
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Workout Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable { showDatePicker = true }
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("OK")
                    }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}


private fun convertMillisToDate(millis: Long): String =
    Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date.toString()

