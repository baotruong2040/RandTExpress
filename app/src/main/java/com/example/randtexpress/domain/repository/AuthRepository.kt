package com.example.randtexpress.domain.repository

import com.example.randtexpress.data.remote.dto.response.AuthResponse

interface AuthRepository {
    suspend fun register(
        username: String,
        password: String,
        fullName: String,
        email: String,
        phone: String,
        address: String? = null
    ): AuthResponse

    suspend fun login(
        username: String,
        password: String
    ): AuthResponse
}
