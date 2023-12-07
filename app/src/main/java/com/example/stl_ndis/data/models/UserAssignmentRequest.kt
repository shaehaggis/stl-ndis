package com.example.stl_ndis.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserAssignmentRequest(
    val input_userid: String
)
