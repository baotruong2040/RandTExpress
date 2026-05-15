package com.example.randtexpress.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @SerializedName("delivery_address")
    val deliveryAddress: String,
    @SerializedName("items")
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("quantity")
    val quantity: Int
)

data class UpdateOrderStatusRequest(
    @SerializedName("status")
    val status: String
)
