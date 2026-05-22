package com.example.randtexpress.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class SearchProductsResponse(
    @SerializedName("products")
    val products: List<ProductResponse>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total")
    val total: Int
)
