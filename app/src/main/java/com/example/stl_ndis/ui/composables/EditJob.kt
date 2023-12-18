package com.example.stl_ndis.ui.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.ui.state.SaveJobStatus
import com.example.stl_ndis.ui.viewmodels.EditJobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditJob(
    modifier: Modifier = Modifier,
    navigateToHome: (Boolean) -> Unit,
    editJobViewModel: EditJobViewModel
){

    val saveJobStatus: SaveJobStatus by editJobViewModel.saveJobStatus.collectAsState()

    val currentJob: NDISJob by editJobViewModel.currentJob.collectAsState()

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TopAppBar(
                title = { Text(text = "Edit Job") },
                navigationIcon = {
                    IconButton(onClick = { navigateToHome(false) }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
        item {
            Text(text = "Select Date")
            Spacer(modifier = Modifier.height(12.dp))
            DatePickerComposableEdit(viewModel = editJobViewModel)
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = "Select Time")
            Spacer(modifier = Modifier.height(12.dp))
            TimePickerComposableEdit(viewModel = editJobViewModel)
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))

            LabeledTextField(
                label = "Select Approximate Hours",
                value = currentJob.hours.toString(),
                onValueChange = { editJobViewModel.setHours(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }

        item {

            Spacer(modifier = Modifier.height(40.dp))

            LabeledTextField(
                label = "Select Category of Service",
                value = currentJob.serviceCategory,
                onValueChange = { editJobViewModel.setServiceCategory(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

        }
        item {
            Spacer(modifier = Modifier.height(40.dp))

            LabeledTextField(
                label = "Pickup Location",
                value = currentJob.pickupLocation,
                onValueChange = { editJobViewModel.setPickupLocation(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
        }

        item {

            Spacer(modifier = Modifier.height(40.dp))

            LabeledTextField(
                label = "Description of Job (Optional)",
                value = currentJob.description,
                onValueChange = { editJobViewModel.setJobDescription(it) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }

        itemsIndexed(editJobViewModel.supportWorkers) { index, supportWorkerId ->
            LabeledTextField(
                label = "Support Worker Assigned",
                value = supportWorkerId,
                onValueChange = { updatedId ->
                    editJobViewModel.updateSupportWorker(index, updatedId)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            IconButton(onClick = { editJobViewModel.removeSupportWorker(index) }) {
                Icon(Icons.Filled.Clear, contentDescription = "Remove")
            }
        }

        item {
            Button(onClick = { editJobViewModel.addSupportWorker() }) {
                Text(text = "Add Support Worker")
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { editJobViewModel.saveJob() }) {
                Text(text = "Save Job")
            }
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }

    when (saveJobStatus){
        is SaveJobStatus.Success -> {
            editJobViewModel.resetSaveJobState()
            navigateToHome(true)
        }
        is SaveJobStatus.Failure -> {
            Toast.makeText(LocalContext.current, "Error Saving Job", Toast.LENGTH_SHORT).show()
        }
        is SaveJobStatus.Loading -> {
            CircularProgressIndicator()
        }
        is SaveJobStatus.Idle -> {}
    }
}