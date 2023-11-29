package com.example.stl_ndis.data.repositories

import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.data.models.NDISJobDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JobRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    private val _jobsFlow = MutableStateFlow<List<NDISJobDTO>?>(listOf())
    val jobsFlow: StateFlow<List<NDISJobDTO>?> = _jobsFlow.asStateFlow()

    suspend fun initialiseJobs() {
        repositoryScope.launch {
            updateJobsFlow()
        }
    }

    suspend fun saveJobToRemoteDatabase(job: NDISJob): Boolean {
        return try {
            val ndisJobDto = convertDomainToDto(job)

            supabaseClient.postgrest
                .from("jobs")
                .insert(ndisJobDto)

            updateJobsFlow()
            true
        } catch (e: Exception) {
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
        _jobsFlow.value= fetchJobsFromRemoteDatabase()
    }
}