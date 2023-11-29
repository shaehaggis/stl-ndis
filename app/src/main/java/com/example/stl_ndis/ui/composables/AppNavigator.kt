package com.example.stl_ndis.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stl_ndis.ui.viewmodels.HomeViewModel
import com.example.stl_ndis.ui.viewmodels.JobCreationViewModel
import com.example.stl_ndis.ui.viewmodels.LoginViewModel
import com.example.stl_ndis.ui.viewmodels.ViewJobsViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator(
    navController: NavHostController,
    startDestination: String,
    supabaseClient: SupabaseClient
) {

    val loginViewModel: LoginViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val jobCreationViewModel: JobCreationViewModel = hiltViewModel()
    val viewJobsViewModel: ViewJobsViewModel = hiltViewModel()

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
                    navigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    logout = { supabaseClient.gotrue.logout() },
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
                ViewJobs(
                    Modifier.fillMaxSize(),
                    navigateToHome = {navController.navigate("home")},
                    viewJobsViewModel = viewJobsViewModel
                )
            }
        }
    )
}