package com.example.randtexpress.data.repository

import com.example.randtexpress.data.remote.api.AuthApiService
import com.example.randtexpress.data.remote.dto.request.LoginRequest
import com.example.randtexpress.data.remote.dto.request.RegisterRequest
import com.example.randtexpress.data.remote.dto.response.AuthResponse
import com.example.randtexpress.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun register(
        username: String,
        password: String,
        fullName: String,
        email: String,
        phone: String,
        address: String?
    ): AuthResponse {
        val request = RegisterRequest(
            username = username,
            password = password,
            fullName = fullName,
            email = email,
            phone = phone,
            address = address
        )
        val response = authApiService.register(request)
        return response.data ?: throw Exception(response.message)
    }

    override suspend fun login(
        username: String,
        password: String
    ): AuthResponse {
        val request = LoginRequest(username = username, password = password)
        val response = authApiService.login(request)
        return response.data ?: throw Exception(response.message)
    }
}
