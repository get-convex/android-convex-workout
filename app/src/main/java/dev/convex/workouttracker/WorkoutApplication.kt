package dev.convex.workouttracker

import android.app.Application
import dev.convex.android.ConvexClientWithAuth
import dev.convex.android.auth0.Auth0Provider
import dev.convex.workouttracker.core.WorkoutRepository

class WorkoutApplication : Application() {
    lateinit var repository: WorkoutRepository

    override fun onCreate() {
        super.onCreate()
        repository = WorkoutRepository(
            convex = ConvexClientWithAuth(
                getString(R.string.convex_url),
                Auth0Provider(
                    getString(R.string.com_auth0_client_id),
                    getString(R.string.com_auth0_domain),
                    getString(R.string.com_auth0_scheme)
                )
            )
        )
    }
}