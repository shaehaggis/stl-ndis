package com.example.stl_ndis.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.ui.viewmodels.ViewJobsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewJobs(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    viewJobsViewModel: ViewJobsViewModel
){

    val jobList by viewJobsViewModel.jobList.collectAsState(initial = listOf())

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Arrow",
                    modifier = Modifier.clickable(onClick = navigateToHome)

                )
                Text(
                    text = "Jobs",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Divider(color = Color.Gray, thickness = 0.5.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Date", fontWeight = FontWeight.Bold)
                Text(text = "Category", fontWeight = FontWeight.Bold)
                Text(text = "Job Status", fontWeight = FontWeight.Bold)
            }

            Divider(color = Color.Gray, thickness = 0.5.dp)

            LazyColumn {
                items(jobList.orEmpty()) { job ->
                    JobRow(job = job)
                    Divider(color = Color.Gray, thickness = 0.5.dp)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobRow(
    job: NDISJob
) {

    val dateFormatterDisplay = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val jobDateDisplay = LocalDate.parse(job.startDate).format(dateFormatterDisplay)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = jobDateDisplay)
        Text(text = job.serviceCategory)
        Text(text = job.status)
    }
}