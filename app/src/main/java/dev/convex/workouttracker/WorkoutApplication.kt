package dev.convex.workouttracker

import android.app.Application
import com.auth0.android.result.Credentials
import dev.convex.android.ConvexClientWithAuth
import dev.convex.android.auth0.Auth0Provider

class WorkoutApplication : Application() {
    lateinit var convexClient: ConvexClientWithAuth<Credentials>
    override fun onCreate() {
        super.onCreate()
        convexClient = ConvexClientWithAuth(
            getString(R.string.convex_url),
            Auth0Provider(
                getString(R.string.com_auth0_client_id),
                getString(R.string.com_auth0_domain),
                getString(R.string.com_auth0_scheme)
            )
        )
    }
}