package com.example.randtexpress.data.repository

import com.example.randtexpress.data.remote.api.ProductApiService
import com.example.randtexpress.data.remote.dto.response.ProductListResponse
import com.example.randtexpress.data.remote.dto.response.ProductResponse
import com.example.randtexpress.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApiService: ProductApiService
) : ProductRepository {

    override suspend fun getProducts(
        categoryId: Int?,
        page: Int,
        pageSize: Int
    ): ProductListResponse {
        val response = productApiService.getProducts(
            categoryId = categoryId,
            page = page,
            pageSize = pageSize
        )
        return response.data ?: throw Exception(response.message)
    }

    override suspend fun getProductById(productId: Int): ProductResponse {
        val response = productApiService.getProductById(productId)
        return response.data ?: throw Exception(response.message)
    }
}
