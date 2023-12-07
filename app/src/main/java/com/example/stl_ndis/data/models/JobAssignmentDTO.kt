package com.example.stl_ndis.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobAssignmentDTO(
    @SerialName("assignmentid")
    val assignmentID: Int? = null,

    @SerialName("support_worker_id")
    val supportWorkerID: String?,

    @SerialName("first_name")
    val firstName: String?,

    @SerialName("surname")
    val surname: String?,

    @SerialName("jobid")
    val jobID: String?,

    @SerialName("rating")
    val rating: Int? = null,

    @SerialName("feedback")
    val feedback: String? = null
)
