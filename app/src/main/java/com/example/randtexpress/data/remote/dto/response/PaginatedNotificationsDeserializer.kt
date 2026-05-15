package com.example.randtexpress.data.remote.dto.response

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class PaginatedNotificationsDeserializer : JsonDeserializer<PaginatedNotifications> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PaginatedNotifications {
        return when {
            json.isJsonArray -> {
                // Backend returning direct array: [...]
                val notifications = context.deserialize<List<NotificationResponse>>(
                    json,
                    object : com.google.gson.reflect.TypeToken<List<NotificationResponse>>() {}.type
                )
                PaginatedNotifications(
                    notifications = notifications,
                    pagination = NotificationPaginationInfo(page = 1, pageSize = notifications.size, total = notifications.size, unreadCount = 0)
                )
            }
            json.isJsonObject -> {
                // Backend returning object: {notifications: [...], pagination: {...}}
                context.deserialize(json, PaginatedNotifications::class.java)
            }
            else -> throw JsonParseException("Expected array or object for PaginatedNotifications")
        }
    }
}
