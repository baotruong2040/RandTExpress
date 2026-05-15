package com.example.randtexpress.data.repository

import com.example.randtexpress.data.remote.api.OrderApiService
import com.example.randtexpress.data.remote.dto.request.CreateOrderRequest
import com.example.randtexpress.data.remote.dto.request.OrderItemRequest
import com.example.randtexpress.data.remote.dto.request.UpdateOrderStatusRequest
import com.example.randtexpress.data.remote.dto.response.OrderDetailsResponse
import com.example.randtexpress.data.remote.dto.response.OrderListResponse
import com.example.randtexpress.data.remote.dto.response.OrderResponse
import com.example.randtexpress.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApiService: OrderApiService
) : OrderRepository {

    override suspend fun createOrder(
        deliveryAddress: String,
        items: List<Pair<Int, Int>>
    ): OrderResponse {
        val orderItems = items.map { (productId, quantity) ->
            OrderItemRequest(productId = productId, quantity = quantity)
        }
        val request = CreateOrderRequest(
            deliveryAddress = deliveryAddress,
            items = orderItems
        )
        val response = orderApiService.createOrder(request)
        return response.data ?: throw Exception(response.message)
    }

    override suspend fun getOrders(
        status: String?,
        page: Int,
        pageSize: Int
    ): OrderListResponse {
        val response = orderApiService.getOrders(
            status = status,
            page = page,
            pageSize = pageSize
        )
        return response.data ?: throw Exception(response.message)
    }

    override suspend fun getOrderById(orderId: Int): OrderDetailsResponse {
        val response = orderApiService.getOrderById(orderId)
        return response.data ?: throw Exception(response.message)
    }

    override suspend fun updateOrderStatus(orderId: Int, status: String): OrderResponse {
        val request = UpdateOrderStatusRequest(status = status)
        val response = orderApiService.updateOrderStatus(orderId, request)
        return response.data ?: throw Exception(response.message)
    }
}
