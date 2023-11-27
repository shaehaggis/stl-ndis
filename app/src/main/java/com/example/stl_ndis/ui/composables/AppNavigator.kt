package com.example.stl_ndis.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stl_ndis.ui.viewmodels.HomeViewModel
import com.example.stl_ndis.ui.viewmodels.JobCreationViewModel
import com.example.stl_ndis.ui.viewmodels.LoginViewModel

@Composable
fun AppNavigator(
    navController: NavHostController,
    startDestination: String
) {

    val loginViewModel: LoginViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val jobCreationViewModel: JobCreationViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable("login") {
                Login(
                    modifier = Modifier.fillMaxSize(),
                    navigateToHome = { navController.navigate("home") },
                    loginViewModel = loginViewModel
                )
            }

            composable("home") {
                HomePage(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    navigateToCreateJob = { navController.navigate("create-job") },
                    navigateToViewJobs = { navController.navigate("view-jobs") },
                    homeViewModel = homeViewModel
                )
            }

            composable("create-job") {
                CreateJob(
                    Modifier.fillMaxSize(),
                    navigateToHome =
                        { showToast ->
                            if (showToast) {
                                homeViewModel.showToast()
                            }
                            navController.navigate("home")
                        },
                    jobCreationViewModel = jobCreationViewModel
                )
            }

            composable("view-jobs") {
                Column {
                    Text(text = "Hello World - view jobs")
                    Button(
                        onClick = { navController.navigate("home") }
                    ) {
                        Text(text = "Navigate to home")
                    }
                    Button(
                        onClick = { navController.navigate("create-job") }
                    ) {
                        Text(text = "Navigate to create jobs")
                    }
                    Button(
                        onClick = { navController.navigate("login") }
                    ) {
                        Text(text = "Navigate to login")
                    }
                }
            }
        }
    )
}