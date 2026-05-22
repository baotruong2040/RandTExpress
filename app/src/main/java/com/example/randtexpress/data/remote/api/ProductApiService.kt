package com.example.randtexpress.data.remote.api

import com.example.randtexpress.data.remote.dto.response.ApiResponse
import com.example.randtexpress.data.remote.dto.response.ProductListResponse
import com.example.randtexpress.data.remote.dto.response.ProductResponse
import com.example.randtexpress.data.remote.dto.response.SearchProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("category_id") categoryId: Int? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10
    ): ApiResponse<ProductListResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): ApiResponse<ProductResponse>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String? = null,
        @Query("category_ids") categoryIds: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("sort_order") sortOrder: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): ApiResponse<SearchProductsResponse>
}
