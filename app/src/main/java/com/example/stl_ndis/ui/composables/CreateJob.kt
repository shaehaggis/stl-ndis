package com.example.stl_ndis.ui.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stl_ndis.ui.state.SaveJobStatus
import com.example.stl_ndis.ui.viewmodels.JobCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJob(
    modifier: Modifier = Modifier,
    navigateToHome: (Boolean) -> Unit,
    jobCreationViewModel: JobCreationViewModel
){

    val saveJobStatus: SaveJobStatus by jobCreationViewModel.saveJobStatus.collectAsState()

    when (saveJobStatus){
        is SaveJobStatus.Success -> {
            jobCreationViewModel.resetSaveJobState()
            navigateToHome(true)
        }
        is SaveJobStatus.Failure -> {
            Toast.makeText(LocalContext.current, "Error Saving Job", Toast.LENGTH_SHORT).show()
        }
        is SaveJobStatus.Idle -> {}
    }

    Column {
        TopAppBar(
            title = { Text(text = "Create Job") },
            navigationIcon = {
                IconButton(onClick = { navigateToHome(false) }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = "Select Date")
                Spacer(modifier = Modifier.height(12.dp))
                DatePickerComposable(viewModel = jobCreationViewModel)

                Spacer(modifier = Modifier.height(40.dp))
                Text(text = "Select Time")
                Spacer(modifier = Modifier.height(12.dp))
                TimePickerComposable(viewModel = jobCreationViewModel)

                Spacer(modifier = Modifier.height(40.dp))

                LabeledTextField(
                    label = "Select Approximate Hours",
                    value = jobCreationViewModel.approxHours,
                    onValueChange = { jobCreationViewModel.approxHours = it },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))

                LabeledTextField(
                    label = "Select Category of Service",
                    value = jobCreationViewModel.categoryOfService,
                    onValueChange = { jobCreationViewModel.categoryOfService = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(40.dp))

                LabeledTextField(
                    label = "Pickup Location",
                    value = jobCreationViewModel.pickupLocation,
                    onValueChange = { jobCreationViewModel.pickupLocation = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(40.dp))

                LabeledTextField(
                    label = "Description of Job (Optional)",
                    value = jobCreationViewModel.jobDescription,
                    onValueChange = { jobCreationViewModel.jobDescription = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(40.dp))

                LabeledTextField(
                    label = "Support Worker Assigned",
                    value = jobCreationViewModel.supportWorkerAssigned,
                    onValueChange = { jobCreationViewModel.supportWorkerAssigned = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                Spacer(modifier = Modifier.height(40.dp))
                Button(onClick = { jobCreationViewModel.saveJob() }) {
                    Text(text = "Create Job")
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledTextField(label: String, value: String, onValueChange: (String) -> Unit, keyboardOptions: KeyboardOptions) {
    Text(text = label)
    Spacer(modifier = Modifier.height(12.dp))
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions
    )
}