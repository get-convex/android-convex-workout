package dev.convex.workouttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.convex.workouttracker.ui.OverviewScreen
import dev.convex.workouttracker.ui.SignInScreen
import dev.convex.workouttracker.ui.WorkoutEditorScreen
import dev.convex.workouttracker.ui.theme.WorkoutTrackerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkoutTrackerTheme {
                val navController = rememberNavController()

                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination
                val currentScreen = workoutTrackerScreens.find {
                    it.route == currentDestination?.route
                } ?: Overview

                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(currentScreen.title)
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = SignIn.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = SignIn.route) {
                            SignInScreen(
                                onClickSignIn = {
                                    navController.navigate(Overview.route)
                                }
                            )
                        }
                        composable(route = Overview.route) {
                            OverviewScreen(
                                onClickAddWorkout = {
                                    navController.navigate(WorkoutEditor.route)
                                }
                            )
                        }
                        composable(route = WorkoutEditor.route) {
                            WorkoutEditorScreen()
                        }
                    }
                }
            }
        }
    }
}
