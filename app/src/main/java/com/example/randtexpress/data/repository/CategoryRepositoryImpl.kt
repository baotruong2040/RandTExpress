package com.example.randtexpress.data.repository

import com.example.randtexpress.data.remote.api.CategoryApiService
import com.example.randtexpress.data.remote.dto.response.CategoryListResponse
import com.example.randtexpress.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryApiService: CategoryApiService
) : CategoryRepository {

    override suspend fun getCategories(): CategoryListResponse {
        val response = categoryApiService.getCategories()
        return response.data ?: throw Exception(response.message)
    }
}
