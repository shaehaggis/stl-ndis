package com.example.stl_ndis.data.helpers

import com.example.stl_ndis.data.models.JobAssignmentDTO
import com.example.stl_ndis.data.models.JobAssignmentInsertionDTO
import com.example.stl_ndis.data.models.LoginCredentials
import com.example.stl_ndis.data.models.NDISJob
import com.example.stl_ndis.data.models.NDISJobDTO
import com.example.stl_ndis.data.models.User
import com.example.stl_ndis.data.models.UserAssignmentRequest
import com.example.stl_ndis.data.models.UserDTO
import com.example.stl_ndis.data.models.toDomain
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject

class SupabaseClientWrapper @Inject constructor(
    private val supabaseClient: SupabaseClient
) {

    private fun getUserID(): String? {
        return supabaseClient.gotrue.currentSessionOrNull()?.user?.id
    }

    suspend fun login(credentials: LoginCredentials) {
        supabaseClient.gotrue.loginWith(Email) {
            email = credentials.username
            password = credentials.password
        }
    }

    suspend fun getUserInfo(): User {

        val userId = getUserID()

        val response = supabaseClient.postgrest
            .from("users")
            .select {
                if (userId != null) {
                    eq("id", userId)
                }
            }
            .decodeSingle<UserDTO>()

        return response.toDomain()
    }

    suspend fun saveJob(ndisJob: NDISJob): NDISJobDTO {
        val ndisJobDTO = convertDomainToDto(ndisJob, getUserID())

        val result = supabaseClient.postgrest
            .from("jobs")
            .insert(ndisJobDTO)
            .decodeSingle<NDISJobDTO>()

        return result
    }

    suspend fun addAssignmentsToJob(job: NDISJobDTO, supportWorkers: List<String>) {

        val assignments = supportWorkers.map { supportWorkerID ->
            JobAssignmentInsertionDTO(
                supportWorkerID = supportWorkerID,
                jobID = job.jobId
            )
        }

        supabaseClient.postgrest
            .from("assignments")
            .insert(assignments)
    }

    suspend fun getAllJobAssignments(): List<JobAssignmentDTO> {

        val userId = getUserID()

        val request = userId?.let { UserAssignmentRequest(it) }
        val jsonString = Json.encodeToString(request)
        val params = Json.parseToJsonElement(jsonString).jsonObject

        val result = supabaseClient.postgrest
            .rpc("get_user_assignments", params)
            .decodeList<JobAssignmentDTO>()

        return result
    }

    suspend fun fetchAllJobs(): List<NDISJobDTO> {

        val userId = getUserID()

        return if (userId != null) {
            val result = supabaseClient.postgrest.from("jobs")
                .select {
                    eq("userid", userId)
                    order("startdate", order = Order.DESCENDING)
                }
                .decodeList<NDISJobDTO>()
            result
        } else {
            emptyList()
        }
    }
}