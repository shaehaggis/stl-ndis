package com.example.stl_ndis.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerialName("id")
    val userID: String? = null,

    @SerialName("first_name")
    val firstName: String? = null,

    @SerialName("surname")
    val surname: String? = null,

    @SerialName("role")
    val role: String? = null,

    @SerialName("created_at")
    val created_at: String? = null,

    @SerialName("updated_at")
    val updated_at: String? = null
)

fun UserDTO.toDomain(): User {
    return User(
        firstName = this.firstName ?: "",
        surname = this.surname ?: "",
        role = this.role ?: ""
    )
}