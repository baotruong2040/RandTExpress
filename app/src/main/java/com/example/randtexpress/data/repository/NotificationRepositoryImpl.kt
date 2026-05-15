package com.example.randtexpress.data.repository

import com.example.randtexpress.data.remote.api.NotificationApiService
import com.example.randtexpress.data.remote.dto.response.NotificationResponse
import com.example.randtexpress.data.remote.dto.response.PaginatedNotifications
import com.example.randtexpress.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApiService: NotificationApiService
) : NotificationRepository {

    override suspend fun getNotifications(
        page: Int,
        pageSize: Int,
        unreadOnly: Boolean?
    ): PaginatedNotifications {
        val response = notificationApiService.getNotifications(
            page = page,
            pageSize = pageSize,
            unreadOnly = unreadOnly
        )
        return response.data ?: throw Exception(response.message)
    }

    override suspend fun markAsRead(notificationId: Int): NotificationResponse {
        val response = notificationApiService.markAsRead(notificationId)
        return response.data ?: throw Exception(response.message)
    }
}
