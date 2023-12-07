package com.example.stl_ndis.ui.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.stl_ndis.data.repositories.JobRepository
import com.example.stl_ndis.ui.composables.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabaseClient: SupabaseClient

    @Inject
    lateinit var jobRepository: JobRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val startDestination = determineStartDestination(supabaseClient, jobRepository)

            AppNavigator(
                navController = navController,
                startDestination = startDestination,
                supabaseClient = supabaseClient
            )
        }
    }

    @Composable
    private fun determineStartDestination(
        supabaseClient: SupabaseClient,
        jobRepository: JobRepository
    ): String {
        return if (supabaseClient.gotrue.currentSessionOrNull() != null) {
            LaunchedEffect(Unit) {
                jobRepository.initialiseJobs()
            }
            "home"
        } else {
            "login"
        }
    }
}