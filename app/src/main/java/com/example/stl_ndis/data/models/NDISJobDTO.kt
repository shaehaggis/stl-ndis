package com.example.stl_ndis.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NDISJobDTO(

    @SerialName("jobid")
    val jobId: String? = null,

    @SerialName("userid")
    val userId: String? = null,

    @SerialName("startdate")
    val startDate: String? = null,

    @SerialName("starttime")
    val startTime: String? = null,

    @SerialName("hours")
    val hours: Int? = null,

    @SerialName("status")
    val status: String? = null,

    @SerialName("servicecategory")
    val serviceCategory: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("pickuplocation")
    val pickupLocation: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

fun NDISJobDTO.asDomainObject(): NDISJob? {
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