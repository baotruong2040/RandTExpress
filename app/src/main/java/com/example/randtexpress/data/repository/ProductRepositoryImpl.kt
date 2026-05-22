package com.example.randtexpress.data.repository

import com.example.randtexpress.data.remote.api.ProductApiService
import com.example.randtexpress.data.remote.dto.response.ProductListResponse
import com.example.randtexpress.data.remote.dto.response.ProductResponse
import com.example.randtexpress.data.remote.dto.response.SearchProductsResponse
import com.example.randtexpress.data.remote.ImageUrlResolver
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
        val data = response.data ?: throw Exception(response.message)
        return data.copy(
            products = data.products.map { product ->
                product.copy(imageUrl = ImageUrlResolver.resolve(product.imageUrl))
            }
        )
    }

    override suspend fun getProductById(productId: Int): ProductResponse {
        val response = productApiService.getProductById(productId)
        val product = response.data ?: throw Exception(response.message)
        return product.copy(imageUrl = ImageUrlResolver.resolve(product.imageUrl))
    }

    override suspend fun searchProducts(
        query: String,
        page: Int,
        pageSize: Int
    ): SearchProductsResponse {
        val response = productApiService.searchProducts(
            query = query,
            page = page,
            pageSize = pageSize
        )
        val data = response.data ?: throw Exception(response.message)
        return data.copy(
            products = data.products.map { product ->
                product.copy(imageUrl = ImageUrlResolver.resolve(product.imageUrl))
            }
        )
    }
}
