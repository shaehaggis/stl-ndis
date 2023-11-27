package com.example.stl_ndis.data.repositories

import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.data.models.NDISJobDTO
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class JobRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {

    suspend fun saveJobToRemoteDatabase(job: NDISJob): Boolean {
        return try {
            val ndisJobDto = convertDomainToDto(job)

            supabaseClient.postgrest
                .from("jobs")
                .insert(ndisJobDto)

//            updateJobsFlow()
            true
        } catch (e: Exception) {
            false
        }
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

}