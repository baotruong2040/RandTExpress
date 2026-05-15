package com.example.randtexpress.data.remote.api

import com.example.randtexpress.data.remote.dto.request.CreateOrderRequest
import com.example.randtexpress.data.remote.dto.request.UpdateOrderStatusRequest
import com.example.randtexpress.data.remote.dto.response.ApiResponse
import com.example.randtexpress.data.remote.dto.response.OrderDetailsResponse
import com.example.randtexpress.data.remote.dto.response.OrderListResponse
import com.example.randtexpress.data.remote.dto.response.OrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService {

    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): ApiResponse<OrderResponse>

    @GET("orders")
    suspend fun getOrders(
        @Query("status") status: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10
    ): ApiResponse<OrderListResponse>

    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") orderId: Int): ApiResponse<OrderDetailsResponse>

    @POST("orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") orderId: Int,
        @Body request: UpdateOrderStatusRequest
    ): ApiResponse<OrderResponse>
}
