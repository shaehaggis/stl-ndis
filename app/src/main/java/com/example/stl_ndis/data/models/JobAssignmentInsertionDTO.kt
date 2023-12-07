package com.example.stl_ndis.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobAssignmentInsertionDTO(
    @SerialName("assignmentid")
    val assignmentID: Int? = null,

    @SerialName("support_worker_id")
    val supportWorkerID: String?,

    @SerialName("jobid")
    val jobID: String?,

    @SerialName("rating")
    val rating: Int? = null,

    @SerialName("feedback")
    val feedback: String? = null
)

