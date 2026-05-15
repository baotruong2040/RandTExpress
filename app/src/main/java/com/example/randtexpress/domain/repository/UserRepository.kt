package com.example.randtexpress.domain.repository

import com.example.randtexpress.data.remote.dto.response.UserListResponse
import com.example.randtexpress.data.remote.dto.response.UserResponse

interface UserRepository {
    suspend fun getUsers(
        role: String? = null,
        page: Int = 1,
        pageSize: Int = 10
    ): UserListResponse

    suspend fun createStaff(
        username: String,
        password: String,
        fullName: String,
        email: String,
        phone: String,
        address: String? = null
    ): UserResponse
}
