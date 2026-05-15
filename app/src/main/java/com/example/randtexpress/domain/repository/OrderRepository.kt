package com.example.randtexpress.domain.repository

import com.example.randtexpress.data.remote.dto.request.CreateOrderRequest
import com.example.randtexpress.data.remote.dto.response.OrderDetailsResponse
import com.example.randtexpress.data.remote.dto.response.OrderListResponse
import com.example.randtexpress.data.remote.dto.response.OrderResponse

interface OrderRepository {
    suspend fun createOrder(deliveryAddress: String, items: List<Pair<Int, Int>>): OrderResponse

    suspend fun getOrders(status: String? = null, page: Int = 1, pageSize: Int = 10): OrderListResponse

    suspend fun getOrderById(orderId: Int): OrderDetailsResponse

    suspend fun updateOrderStatus(orderId: Int, status: String): OrderResponse
}
