package com.example.randtexpress.data.remote.dto.response

import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class ProductListResponseDeserializer : JsonDeserializer<ProductListResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ProductListResponse {
        return when {
            json.isJsonArray -> {
                // Backend returning direct array: [...]
                val products = context.deserialize<List<ProductResponse>>(
                    json,
                    object : com.google.gson.reflect.TypeToken<List<ProductResponse>>() {}.type
                )
                ProductListResponse(
                    products = products,
                    pagination = PaginationInfo(page = 1, pageSize = products.size, total = products.size)
                )
            }
            json.isJsonObject -> {
                // Backend returning object: {products: [...], pagination: {...}}
                context.deserialize(json, ProductListResponse::class.java)
            }
            else -> throw JsonParseException("Expected array or object for ProductListResponse")
        }
    }
}
