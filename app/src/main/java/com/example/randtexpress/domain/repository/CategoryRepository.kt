package com.example.randtexpress.domain.repository

import com.example.randtexpress.data.remote.dto.response.CategoryListResponse

interface CategoryRepository {
    suspend fun getCategories(): CategoryListResponse
}
