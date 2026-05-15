package com.example.randtexpress.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("order_id")
    val orderId: Int? = null,
    @SerializedName("created_at")
    val createdAt: String
)

data class PaginatedNotifications(
    @SerializedName("notifications")
    val notifications: List<NotificationResponse>,
    @SerializedName("pagination")
    val pagination: NotificationPaginationInfo
)

data class NotificationPaginationInfo(
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("unread_count")
    val unreadCount: Int
)
