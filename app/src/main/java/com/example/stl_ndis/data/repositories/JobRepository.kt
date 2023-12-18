package com.example.stl_ndis.data.repositories

import android.util.Log
import com.example.stl_ndis.data.helpers.SupabaseClientWrapper
import com.example.stl_ndis.data.helpers.addAssignmentsToJobs
import com.example.stl_ndis.data.helpers.createHashMap
import com.example.stl_ndis.data.models.JobAssignmentInsertionDTO
import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.data.models.asDomainObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JobRepository @Inject constructor(
    private val supabaseClientWrapper: SupabaseClientWrapper
) {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    private val _jobsFlowDomain = MutableStateFlow<List<NDISJob>?>(listOf())
    val jobsFlowDomain: StateFlow<List<NDISJob>?> = _jobsFlowDomain.asStateFlow()

    suspend fun initialiseJobs() {
        repositoryScope.launch {
            updateJobsFlow()
        }
    }

    suspend fun saveJobToRemoteDatabase(job: NDISJob, supportWorkers: List<String>): Boolean {
        return try {
            val response = supabaseClientWrapper.saveJob(job)

            if (supportWorkers.isNotEmpty()) {
                supabaseClientWrapper.addAssignmentsToJob(response, supportWorkers)
            }

            updateJobsFlow()

            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }

    suspend fun updateExistingJob(job: NDISJob){
        try {
            supabaseClientWrapper.updateJob(job)
            updateJobsFlow()
        } catch (e: Exception) {
            Log.e("JobRepository", "Failed to update job: ${e.message}")
        }
    }

    suspend fun updateAssignmentsForExistingJob(jobID: String, idsToAdd: List<String>, idsToRemove: List<String>){
        val assignmentsToAdd = idsToAdd.map { supportWorkerID ->
            JobAssignmentInsertionDTO(
                supportWorkerID = supportWorkerID,
                jobID = jobID
            )
        }

        try {
            supabaseClientWrapper.updateAssignmentsForExistingJob(jobID, assignmentsToAdd, idsToRemove)
        } catch (e: Exception) {
            Log.e("JobRepository", "Failed to update assignments: ${e.message}")        }
    }

    private suspend fun updateJobsFlow() {

        // get jobs from database and convert to domain objects
        val jobDTOS = supabaseClientWrapper.fetchAllJobs()
        val jobs = jobDTOS.mapNotNull { dto -> dto.asDomainObject() }

        // get job assignments from database and create hash map
        val result = supabaseClientWrapper.getAllJobAssignments()
        val hashMap = createHashMap(jobs)

        // add assignments to jobs list
        val jobsWithAssignments = addAssignmentsToJobs(result, hashMap, jobs)

        // update flow and alert collectors
        _jobsFlowDomain.emit(jobsWithAssignments)
    }
}