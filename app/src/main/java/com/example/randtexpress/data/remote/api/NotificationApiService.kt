package com.example.randtexpress.data.remote.api

import com.example.randtexpress.data.remote.dto.response.ApiResponse
import com.example.randtexpress.data.remote.dto.response.NotificationResponse
import com.example.randtexpress.data.remote.dto.response.PaginatedNotifications
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApiService {

    @GET("notifications")
    suspend fun getNotifications(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 10,
        @Query("unread_only") unreadOnly: Boolean? = null
    ): ApiResponse<PaginatedNotifications>

    @PUT("notifications/{id}/read")
    suspend fun markAsRead(@Path("id") notificationId: Int): ApiResponse<NotificationResponse>
}
