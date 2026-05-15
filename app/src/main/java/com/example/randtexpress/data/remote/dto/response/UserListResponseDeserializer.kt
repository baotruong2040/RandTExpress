package com.example.randtexpress.data.remote.dto.response

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class UserListResponseDeserializer : JsonDeserializer<UserListResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): UserListResponse {
        return when {
            json.isJsonArray -> {
                // Backend returning direct array: [...]
                val users = context.deserialize<List<UserResponse>>(
                    json,
                    object : com.google.gson.reflect.TypeToken<List<UserResponse>>() {}.type
                )
                UserListResponse(
                    users = users,
                    pagination = PaginationInfo(page = 1, pageSize = users.size, total = users.size)
                )
            }
            json.isJsonObject -> {
                // Backend returning object: {users: [...], pagination: {...}}
                context.deserialize(json, UserListResponse::class.java)
            }
            else -> throw JsonParseException("Expected array or object for UserListResponse")
        }
    }
}
