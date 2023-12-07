package com.example.stl_ndis.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.data.repositories.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewJobsViewModel @Inject constructor(
    private val repository: JobRepository
): ViewModel() {
    private val _jobList = MutableStateFlow<List<NDISJob>?>(listOf())
    val jobList: StateFlow<List<NDISJob>?> = _jobList.asStateFlow()

    init {
        println("Running init for viewjobsviewmodel")

        viewModelScope.launch {
            repository.jobsFlowDomain.collect { jobs ->
                jobs.let {
                    _jobList.emit(it)
                }
            }
        }
    }
}