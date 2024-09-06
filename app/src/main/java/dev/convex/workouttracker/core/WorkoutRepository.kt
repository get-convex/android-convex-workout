package dev.convex.workouttracker.core

import android.content.Context
import com.auth0.android.result.Credentials
import dev.convex.android.ConvexClientWithAuth
import dev.convex.workouttracker.models.Workout
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val convex: ConvexClientWithAuth<Credentials>) {
    val authState get() = convex.authState

    suspend fun storeWorkout(workout: Workout) {
        convex.mutation<String?>(
            "workouts:store",
            workout.toArgs()
        )
    }

    suspend fun subscribeToWorkouts(): Flow<Result<List<Workout>>> =
        convex.subscribe<List<Workout>>("workouts:get")

    suspend fun subscribeToWorkoutsInRange(
        startDate: String,
        endDate: String
    ): Flow<Result<List<Workout>>> =
        convex.subscribe<List<Workout>>(
            "workouts:getWorkoutsInRange",
            mapOf("startDate" to startDate, "endDate" to endDate)
        )

    suspend fun signIn(context: Context) {
        convex.login(context)
    }

    suspend fun signInWithCachedCredentials() {
        convex.loginFromCache()
    }
}