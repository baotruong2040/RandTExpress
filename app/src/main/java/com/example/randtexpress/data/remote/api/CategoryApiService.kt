package com.example.randtexpress.data.remote.api

import com.example.randtexpress.data.remote.dto.response.ApiResponse
import com.example.randtexpress.data.remote.dto.response.CategoryListResponse
import com.example.randtexpress.data.remote.dto.response.CategoryResponse
import retrofit2.http.GET

interface CategoryApiService {

    @GET("categories")
    suspend fun getCategories(): ApiResponse<CategoryListResponse>
}
