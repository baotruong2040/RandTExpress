package com.example.randtexpress.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Long,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("is_available")
    val isAvailable: Boolean,
    @SerializedName("created_at")
    val createdAt: String
)

data class ProductListResponse(
    @SerializedName("products")
    val products: List<ProductResponse>,
    @SerializedName("pagination")
    val pagination: PaginationInfo
)

data class PaginationInfo(
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total")
    val total: Int
)
