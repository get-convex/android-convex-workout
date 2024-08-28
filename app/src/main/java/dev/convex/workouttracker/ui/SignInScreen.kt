package dev.convex.workouttracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.convex.workouttracker.ui.theme.WorkoutTrackerTheme

@Composable
fun SignInScreen(
    onClickSignIn: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Workout\nTracker",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
        )
        // TODO: Kick off auth flow
        Button(onClick = onClickSignIn) {
            Text(text = "Sign In")
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 480)
@Composable
fun SignInScreenPreview() {
    WorkoutTrackerTheme {
        SignInScreen()
    }
}