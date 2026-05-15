package com.example.randtexpress.data.remote.api

import com.example.randtexpress.data.remote.dto.request.LoginRequest
import com.example.randtexpress.data.remote.dto.request.RegisterRequest
import com.example.randtexpress.data.remote.dto.response.ApiResponse
import com.example.randtexpress.data.remote.dto.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<AuthResponse>
}
