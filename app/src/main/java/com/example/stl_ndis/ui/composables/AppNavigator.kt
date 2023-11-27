package com.example.stl_ndis.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stl_ndis.ui.viewmodels.LoginViewModel

@Composable
fun AppNavigator(
    navController: NavHostController,
    startDestination: String
) {

    val loginViewModel: LoginViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable("login") {
                Login(
                    navigateToHome = { navController.navigate("home") },
                    loginViewModel = loginViewModel
                )
            }

            composable("home") {
                Column {
                    Text(text = "Hello World - Home")
                    Button(
                        onClick = { navController.navigate("create-job") }
                    ) {
                        Text(text = "Navigate to create jobs")
                    }
                    Button(
                        onClick = { navController.navigate("view-jobs") }
                    ) {
                        Text(text = "Navigate to view jobs")
                    }
                }
            }

            composable("create-job") {
                Column {
                    Text(text = "Hello World - create job")

                    Button(
                        onClick = { navController.navigate("home") }
                    ) {
                        Text(text = "Navigate to home")
                    }
                    Button(
                        onClick = { navController.navigate("login") }
                    ) {
                        Text(text = "Navigate to login")
                    }
                    Button(
                        onClick = { navController.navigate("view-jobs") }
                    ) {
                        Text(text = "Navigate to view jobs")
                    }
                }
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