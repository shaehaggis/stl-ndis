package com.example.stl_ndis.data.models

data class NDISJob(
    val jobId: String,
    val userId: String,
    val startDate: String,
    val startTime: String,
    val hours: Int,
    val status: String,
    val serviceCategory: String,
    val description: String,
    val pickupLocation: String,
    var assignedSupportWorkers: List<SupportWorker>? = listOf()
)
