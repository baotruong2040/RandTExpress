package com.example.randtexpress.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CreateStaffRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address")
    val address: String? = null
)
