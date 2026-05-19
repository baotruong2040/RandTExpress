package com.example.randtexpress.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("created_at")
    val createdAt: String?
)

data class CategoryListResponse(
    @SerializedName("categories")
    val categories: List<CategoryResponse>
)
