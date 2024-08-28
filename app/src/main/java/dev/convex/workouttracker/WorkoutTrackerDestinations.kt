package dev.convex.workouttracker

val workoutTrackerScreens = listOf(SignIn, Overview, WorkoutEditor)

interface WorkoutTrackerDestination {
    val route: String
    val title: String
}

object SignIn : WorkoutTrackerDestination {
    override val route = "onboarding"
    override val title = "Welcome"
}

object Overview : WorkoutTrackerDestination {
    override val route = "overview"
    override val title = "Workout Tracker"
}

object WorkoutEditor : WorkoutTrackerDestination {
    override val route = "workout_editor"
    override val title = "Add Workout"
}