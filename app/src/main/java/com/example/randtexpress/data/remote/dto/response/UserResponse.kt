package com.example.randtexpress.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("created_at")
    val createdAt: String?
)

data class UserListResponse(
    @SerializedName("users")
    val users: List<UserResponse>,
    @SerializedName("pagination")
    val pagination: PaginationInfo
)
