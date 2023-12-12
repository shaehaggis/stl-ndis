package com.example.stl_ndis.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.data.repositories.JobRepository
import com.example.stl_ndis.ui.state.SaveJobStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobCreationViewModel @Inject constructor(
    private val repository: JobRepository,
): ViewModel() {
    var dateOfJob by mutableStateOf("")
    var startTime by mutableStateOf("")
    var approxHours by mutableStateOf("")
    var categoryOfService by mutableStateOf("")
    var pickupLocation by mutableStateOf("")
    var jobDescription by mutableStateOf("")

    var supportWorkers = mutableStateListOf<String>()

    var saveJobStatus: MutableStateFlow<SaveJobStatus> = MutableStateFlow(SaveJobStatus.Idle)

    fun setDate(day: Int, month: Int, year: Int) {
        dateOfJob = String.format("%04d-%02d-%02d", year, month + 1, day)
    }

    fun setTime(hourOfDay: Int, minute: Int) {
        startTime = String.format("%02d:%02d", hourOfDay, minute)
    }

    fun addSupportWorker() {
        println("HI")
        supportWorkers.add("")
    }

    fun removeSupportWorker(index: Int){
        if (index >= 0 && index < supportWorkers.size){
            supportWorkers.removeAt(index)
        }
    }

    fun saveJob(){
        // update state of screen
        saveJobStatus.value = SaveJobStatus.Loading

        // create domain object
        val newJob = NDISJob(
            userId = "temp",
            jobId = "temp",
            startDate = dateOfJob,
            startTime = startTime,
            hours = approxHours.toIntOrNull() ?: 0,
            status = "upcoming",
            serviceCategory = categoryOfService,
            description = jobDescription,
            pickupLocation = pickupLocation
        )

        supportWorkers.forEach { worker ->
            println(worker)
        }

        viewModelScope.launch {
            val isSuccess = repository.saveJobToRemoteDatabase(newJob, supportWorkers)

            if (isSuccess){
                saveJobStatus.value = SaveJobStatus.Success
                dateOfJob = ""
                startTime = ""
                approxHours = ""
                categoryOfService = ""
                pickupLocation = ""
                jobDescription = ""
                supportWorkers.clear()
            }
            else {
                saveJobStatus.value = SaveJobStatus.Failure
            }
        }
    }

    fun resetSaveJobState(){
        saveJobStatus.value = SaveJobStatus.Idle
    }
}