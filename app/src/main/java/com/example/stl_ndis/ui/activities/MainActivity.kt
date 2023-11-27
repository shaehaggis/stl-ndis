package com.example.stl_ndis.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.stl_ndis.ui.composables.AppNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // initialise navController
            val navController = rememberNavController()

            val startDestination = "login"

            AppNavigator(navController = navController, startDestination = startDestination)
        }
    }
}