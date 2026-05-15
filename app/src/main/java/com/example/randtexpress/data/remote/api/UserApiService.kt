package com.example.randtexpress.data.remote.api

import com.example.randtexpress.data.remote.dto.request.CreateStaffRequest
import com.example.randtexpress.data.remote.dto.response.ApiResponse
import com.example.randtexpress.data.remote.dto.response.UserListResponse
import com.example.randtexpress.data.remote.dto.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("role") role: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10
    ): ApiResponse<UserListResponse>

    @POST("users/staff")
    suspend fun createStaff(@Body request: CreateStaffRequest): ApiResponse<UserResponse>
}
