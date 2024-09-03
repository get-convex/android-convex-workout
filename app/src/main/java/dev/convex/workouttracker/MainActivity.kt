package dev.convex.workouttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.convex.android.AuthState
import dev.convex.workouttracker.ui.AuthViewModel
import dev.convex.workouttracker.ui.LoadingScreen
import dev.convex.workouttracker.ui.OverviewScreen
import dev.convex.workouttracker.ui.OverviewViewModel
import dev.convex.workouttracker.ui.SignInScreen
import dev.convex.workouttracker.ui.WorkoutEditorScreen
import dev.convex.workouttracker.ui.WorkoutEditorViewModel
import dev.convex.workouttracker.ui.theme.WorkoutTrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkoutTrackerTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

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
                    val authViewModel: AuthViewModel by viewModels { AuthViewModel.Factory }
                    LaunchedEffect("autoSignIn") {
                        authViewModel.signInAutomatically()
                    }
                    val authState by authViewModel.authState.collectAsState(AuthState.AuthLoading())
                    NavHost(
                        navController = navController,
                        startDestination = when (authState) {
                            is AuthState.AuthLoading -> Loading.route
                            is AuthState.Authenticated -> Overview.route
                            is AuthState.Unauthenticated -> SignIn.route
                        },
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = SignIn.route) {
                            SignInScreen(
                                onClickSignIn = {
                                    coroutineScope.launch {
                                        authViewModel.signIn(this@MainActivity)
                                    }
                                }
                            )
                        }
                        composable(route = Overview.route) {
                            val viewModel: OverviewViewModel by viewModels { OverviewViewModel.Factory }
                            OverviewScreen(
                                onClickAddWorkout = {
                                    navController.navigate(WorkoutEditor.route)
                                },
                                viewModel = viewModel
                            )
                        }
                        composable(route = WorkoutEditor.route) {
                            val viewModel: WorkoutEditorViewModel by viewModels { WorkoutEditorViewModel.Factory }
                            WorkoutEditorScreen(viewModel)
                        }
                        composable(route = Loading.route) {
                            LoadingScreen()
                        }
                    }
                }
            }
        }
    }
}
