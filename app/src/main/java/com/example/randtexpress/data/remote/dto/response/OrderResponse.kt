package com.example.randtexpress.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("order_id")
    val orderId: Int?,
    @SerializedName("total_amount")
    val totalAmount: Long?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("delivery_address")
    val deliveryAddress: String?,
    @SerializedName("created_at")
    val createdAt: String?
)

data class OrderDetailsResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("user_id")
    val userId: Int?,
    @SerializedName("user_name")
    val userName: String?,
    @SerializedName("delivery_address")
    val deliveryAddress: String?,
    @SerializedName("total_amount")
    val totalAmount: Long?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("items")
    val items: List<OrderItemResponse>?
)

data class OrderItemResponse(
    @SerializedName("product_id")
    val productId: Int?,
    @SerializedName("product_name")
    val productName: String?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("price_at_order")
    val priceAtOrder: Long?,
    @SerializedName("subtotal")
    val subtotal: Long?
)

data class OrderListResponse(
    @SerializedName("orders")
    val orders: List<OrderResponse>,
    @SerializedName("pagination")
    val pagination: PaginationInfo
)
