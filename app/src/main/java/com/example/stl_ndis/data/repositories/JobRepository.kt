package com.example.stl_ndis.data.repositories

import com.example.stl_ndis.data.helpers.SupabaseClientWrapper
import com.example.stl_ndis.data.helpers.addAssignmentsToJobs
import com.example.stl_ndis.data.helpers.createHashMap
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

    suspend fun saveJobToRemoteDatabase(job: NDISJob, supportWorkerID: String?): Boolean {
        return try {
            val response = supabaseClientWrapper.saveJob(job)

            if (supportWorkerID != null) {
                supabaseClientWrapper.addAssignmentsToJob(response, supportWorkerID)
            }

            updateJobsFlow()

            true
        } catch (e: Exception) {
            println(e)
            false
        }
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