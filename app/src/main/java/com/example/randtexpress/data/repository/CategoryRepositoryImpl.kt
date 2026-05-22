package com.example.randtexpress.data.repository

import com.example.randtexpress.data.remote.api.CategoryApiService
import com.example.randtexpress.data.remote.dto.response.CategoryListResponse
import com.example.randtexpress.data.remote.ImageUrlResolver
import com.example.randtexpress.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryApiService: CategoryApiService
) : CategoryRepository {

    override suspend fun getCategories(): CategoryListResponse {
        val response = categoryApiService.getCategories()
        val data = response.data ?: throw Exception(response.message)
        return data.copy(
            categories = data.categories.map { category ->
                category.copy(imageUrl = ImageUrlResolver.resolve(category.imageUrl))
            }
        )
    }
}
