package com.example.randtexpress.domain.repository

import com.example.randtexpress.data.remote.dto.response.ProductListResponse
import com.example.randtexpress.data.remote.dto.response.ProductResponse
import com.example.randtexpress.data.remote.dto.response.SearchProductsResponse

interface ProductRepository {
    suspend fun getProducts(
        categoryId: Int? = null,
        page: Int = 1,
        pageSize: Int = 10
    ): ProductListResponse

    suspend fun getProductById(productId: Int): ProductResponse

    suspend fun searchProducts(
        query: String,
        page: Int = 1,
        pageSize: Int = 20
    ): SearchProductsResponse
}
