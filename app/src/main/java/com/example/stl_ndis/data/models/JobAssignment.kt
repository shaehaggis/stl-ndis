package com.example.stl_ndis.data.models

data class JobAssignment(
    val supportWorkerID: String,
    val jobID: String,
    val rating: Int? = null,
    val feedback: String? = null
)
