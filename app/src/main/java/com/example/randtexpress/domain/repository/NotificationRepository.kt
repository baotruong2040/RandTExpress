package com.example.randtexpress.domain.repository

import com.example.randtexpress.data.remote.dto.response.NotificationResponse
import com.example.randtexpress.data.remote.dto.response.PaginatedNotifications

interface NotificationRepository {
    suspend fun getNotifications(
        page: Int = 1,
        pageSize: Int = 10,
        unreadOnly: Boolean? = null
    ): PaginatedNotifications

    suspend fun markAsRead(notificationId: Int): NotificationResponse
}
