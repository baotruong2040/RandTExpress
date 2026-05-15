package com.example.randtexpress.data.remote.dto.response

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class CategoryListResponseDeserializer : JsonDeserializer<CategoryListResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CategoryListResponse {
        return when {
            json.isJsonArray -> {
                // Backend returning direct array: [...]
                val categories = context.deserialize<List<CategoryResponse>>(
                    json,
                    object : com.google.gson.reflect.TypeToken<List<CategoryResponse>>() {}.type
                )
                CategoryListResponse(categories = categories)
            }
            json.isJsonObject -> {
                // Backend returning object: {categories: [...]}
                context.deserialize(json, CategoryListResponse::class.java)
            }
            else -> throw JsonParseException("Expected array or object for CategoryListResponse")
        }
    }
}
