package com.example.randtexpress.data.repository

import com.example.randtexpress.data.remote.api.UserApiService
import com.example.randtexpress.data.remote.dto.request.CreateStaffRequest
import com.example.randtexpress.data.remote.dto.response.UserListResponse
import com.example.randtexpress.data.remote.dto.response.UserResponse
import com.example.randtexpress.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getUsers(
        role: String?,
        page: Int,
        pageSize: Int
    ): UserListResponse {
        val response = userApiService.getUsers(
            role = role,
            page = page,
            pageSize = pageSize
        )
        return response.data ?: throw Exception(response.message)
    }

    override suspend fun createStaff(
        username: String,
        password: String,
        fullName: String,
        email: String,
        phone: String,
        address: String?
    ): UserResponse {
        val request = CreateStaffRequest(
            username = username,
            password = password,
            fullName = fullName,
            email = email,
            phone = phone,
            address = address
        )
        val response = userApiService.createStaff(request)
        return response.data ?: throw Exception(response.message)
    }
}
