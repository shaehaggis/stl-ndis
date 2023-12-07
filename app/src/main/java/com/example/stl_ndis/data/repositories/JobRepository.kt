package com.example.stl_ndis.data.repositories

import com.example.stl_ndis.data.models.JobAssignmentDTO
import com.example.stl_ndis.data.models.JobAssignmentInsertionDTO
import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.data.models.NDISJobDTO
import com.example.stl_ndis.data.models.SupportWorker
import com.example.stl_ndis.data.models.UserAssignmentRequest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject

class JobRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    private val _jobsFlowDomain = MutableStateFlow<List<NDISJob>?>(listOf())
    val jobsFlowDomain: StateFlow<List<NDISJob>?> = _jobsFlowDomain.asStateFlow()

    suspend fun initialiseJobs() {
        println("Running initialise jobs in job repository")

        repositoryScope.launch {
            // update the jobsFlowDomain
            // will this suspend?
            updateJobsFlow()

            // get the user id
            val userId = supabaseClient.gotrue.currentSessionOrNull()?.user?.id

            // fetch assignments
            val request = userId?.let { UserAssignmentRequest(it) }
            val jsonString = Json.encodeToString(request)
            val params = Json.parseToJsonElement(jsonString).jsonObject


            val result = supabaseClient.postgrest
                .rpc("get_user_assignments", params)
                .decodeList<JobAssignmentDTO>()

            println(result)

            // create a hash table from the current jobs list
            // hopefully this creates a hash map of the updated jobs flow
            val hashMap = createHashMap()

            // make the jobs flow into a mutable list
            // in order to add on the support workers
            val jobs = jobsFlowDomain.value?.toMutableList() ?: mutableListOf()

            // iterate through each of the assignment DTOs
            result.forEach { assignment ->
                if (assignment.supportWorkerID != null && assignment.firstName != null && assignment.surname != null){

                    // create a new support worker object
                    val supportWorker = SupportWorker(
                        supportWorkerID = assignment.supportWorkerID,
                        firstName = assignment.firstName,
                        surname = assignment.surname
                    )

                    // find which job to add the assignment to
                    // via the index found in the hashmap
                    val jobIndex = hashMap[assignment.jobID]


                    // add the support worker to the list of assigned support workers for that job
                    jobIndex?.let { index ->
                        val job = jobs.getOrNull(index)
                        job?.let {
                            it.assignedSupportWorkers = it.assignedSupportWorkers.orEmpty() + supportWorker
                            jobs[index] = it
                        }
                    }
                }
            }

            // update the flow with the assignments
            _jobsFlowDomain.value = jobs
        }
    }

    private fun createHashMap(): HashMap<String, Int> {

        // empty hash map
        val hashMap = HashMap<String, Int>()

        // iterate through the jobs flow
        // populate the hash map
        jobsFlowDomain.value?.forEachIndexed { index, ndisJob ->
            ndisJob.jobId.let { jobId ->
                hashMap[jobId] = index
            }
        }

        return hashMap
    }

    suspend fun saveJobToRemoteDatabase(job: NDISJob, supportWorkerID: String?): Boolean {
        return try {
            val ndisJobDto = convertDomainToDto(job)

            val response = supabaseClient.postgrest
                .from("jobs")
                .insert(ndisJobDto)
                .decodeSingle<NDISJobDTO>()

            println(response.jobId)
            println(supportWorkerID)

            // create an assignment DTO
            if (supportWorkerID != null) {
                val newAssignmentDTO = JobAssignmentInsertionDTO(
                    supportWorkerID = supportWorkerID,
                    jobID = response.jobId
                )

                supabaseClient.postgrest
                    .from("assignments")
                    .insert(newAssignmentDTO)
            }

            updateJobsFlow()

            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }

    private suspend fun fetchJobsFromRemoteDatabase(): List<NDISJobDTO> {

        val userId = supabaseClient.gotrue.currentSessionOrNull()?.user?.id

        println("fetching jobs")
        println(userId)

        val result = supabaseClient.postgrest.from("jobs")
            .select {
                if (userId != null) {
                    eq("userid", userId)
                }
                order("startdate", order = Order.DESCENDING)
            }
            .decodeList<NDISJobDTO>()

        return result
    }

    private fun convertDomainToDto(job: NDISJob): NDISJobDTO {
        return NDISJobDTO(
            userId = supabaseClient.gotrue.currentSessionOrNull()?.user?.id,
            startDate = job.startDate,
            startTime = job.startTime,
            hours = job.hours,
            status = job.status,
            serviceCategory = job.serviceCategory,
            description = job.description,
            pickupLocation = job.pickupLocation
        )
    }

    private suspend fun updateJobsFlow() {
        val jobDTOS = fetchJobsFromRemoteDatabase()

        // convert dtos to domain objects
        jobDTOS.let {
            _jobsFlowDomain.emit(it.mapNotNull { dto -> dto.asDomainObject() })
        }
    }

    private fun NDISJobDTO.asDomainObject(): NDISJob? {
        if (jobId == null || userId == null || startDate == null || startTime == null ||
            hours == null || status == null || serviceCategory == null ||
            description == null || pickupLocation == null) {
            return null
        }

        return NDISJob(
            jobId = jobId,
            userId = userId,
            startDate = startDate,
            startTime = startTime,
            hours = hours,
            status = status,
            serviceCategory = serviceCategory,
            description = description,
            pickupLocation = pickupLocation
        )
    }
}