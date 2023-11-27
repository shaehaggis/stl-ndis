package com.example.stl_ndis.ui.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stl_ndis.ui.viewmodels.HomeViewModel

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navigateToCreateJob: () -> Unit,
    navigateToViewJobs: () -> Unit,
    homeViewModel: HomeViewModel
) {

    val showToast = homeViewModel.showToast.collectAsState()

    if (showToast.value){
        Toast.makeText(LocalContext.current, "Saved Job", Toast.LENGTH_SHORT).show()
        homeViewModel.hideToast()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello World!",
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { navigateToCreateJob() }) {
                Text(text = "Create Job")
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = { navigateToViewJobs() }) {
                Text(text = "View Jobs")
            }
        }
    }
}