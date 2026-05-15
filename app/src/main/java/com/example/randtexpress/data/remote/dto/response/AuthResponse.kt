package com.example.randtexpress.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("role")
    val role: String
)
