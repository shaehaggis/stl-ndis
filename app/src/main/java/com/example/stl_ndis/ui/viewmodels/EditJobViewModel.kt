package com.example.stl_ndis.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.data.repositories.JobRepository
import com.example.stl_ndis.ui.state.SaveJobStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditJobViewModel @Inject constructor(
    private val jobRepository: JobRepository
): ViewModel() {
    private val _currentJob = MutableStateFlow(
        NDISJob(
            jobId = "",
            userId = "",
            startDate = "",
            startTime = "",
            hours = 0,
            status = "",
            serviceCategory = "",
            description = "",
            pickupLocation = "",
            assignedSupportWorkers = listOf()
        )
    )

    val currentJob: StateFlow<NDISJob> = _currentJob.asStateFlow()

    var supportWorkers = mutableStateListOf<String>()

    var saveJobStatus: MutableStateFlow<SaveJobStatus> = MutableStateFlow(SaveJobStatus.Idle)

    fun setCurrentJob(job: NDISJob) {
        _currentJob.value = job

        // set the support workers mutable list to be the ids of the job
        supportWorkers.clear()
        job.assignedSupportWorkers?.forEach {supportWorker ->
            supportWorkers.add(supportWorker.supportWorkerID)
        }
    }

    fun setDate(day: Int, month: Int, year: Int) {
        val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
        _currentJob.value = _currentJob.value.copy(startDate = formattedDate)
    }

    fun setTime(hourOfDay: Int, minute: Int) {
        val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
        _currentJob.value = _currentJob.value.copy(startTime = formattedTime)
    }

    fun setHours(hours: String) {
        val hoursInt = hours.toIntOrNull() ?: 0

        _currentJob.value = _currentJob.value.copy(hours = hoursInt)
    }

    fun setStatus(status: String) {
        _currentJob.value = _currentJob.value.copy(status = status)
    }

    fun setServiceCategory(category: String) {
        _currentJob.value = _currentJob.value.copy(serviceCategory = category)
    }

    fun setPickupLocation(location: String) {
        _currentJob.value = _currentJob.value.copy(pickupLocation = location)
    }

    fun setJobDescription(description: String) {
        _currentJob.value = _currentJob.value.copy(description = description)
    }

    fun addSupportWorker() {
        supportWorkers.add("")
    }

    fun removeSupportWorker(index: Int) {
        if (index >= 0 && index < supportWorkers.size) {
            supportWorkers.removeAt(index)
        }
    }

    fun updateSupportWorker(index: Int, supportWorker: String) {
        if (index >= 0 && index < supportWorkers.size) {
            supportWorkers[index] = supportWorker
        }
    }


    private fun getSupportWorkerDifferences(): Pair<List<String>, List<String>> {
        val previousAssignedIds = _currentJob.value.assignedSupportWorkers?.map { it.supportWorkerID } ?: emptyList()
        val modifiedIds = supportWorkers

        val idsToAdd = modifiedIds.filter { it !in previousAssignedIds }
        val idsToRemove = previousAssignedIds.filter { it !in modifiedIds }

        return Pair(idsToAdd, idsToRemove)
    }

    private fun updateSupportWorkers(jobID: String) {
        val (idsToAdd, idsToRemove) = getSupportWorkerDifferences()

        if (idsToAdd.isNotEmpty() || idsToRemove.isNotEmpty()) {
            viewModelScope.launch {
                jobRepository.updateAssignmentsForExistingJob(jobID, idsToAdd, idsToRemove)
            }
        }
    }

    fun saveJob() {
        saveJobStatus.value = SaveJobStatus.Loading

        viewModelScope.launch {
            try {
                updateSupportWorkers(currentJob.value.jobId)
                jobRepository.updateExistingJob(currentJob.value)
                saveJobStatus.value = SaveJobStatus.Success
            }
            catch (e: Exception) {
                saveJobStatus.value = SaveJobStatus.Failure
                Log.e("EditJobViewModel", "Error saving job", e)
            }
        }
    }

    fun resetSaveJobState(){
        saveJobStatus.value = SaveJobStatus.Idle
    }
}