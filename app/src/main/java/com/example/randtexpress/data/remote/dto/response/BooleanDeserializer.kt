package com.example.randtexpress.data.remote.dto.response

import com.google.gson.*
import java.lang.reflect.Type

class BooleanDeserializer : JsonDeserializer<Boolean> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Boolean {
        return when {
            json.isJsonPrimitive -> {
                val primitive = json.asJsonPrimitive
                when {
                    primitive.isBoolean -> primitive.asBoolean
                    primitive.isNumber -> primitive.asNumber.toInt() != 0
                    primitive.isString -> {
                        val string = primitive.asString.lowercase()
                        string == "true" || string == "1"
                    }
                    else -> false
                }
            }
            else -> false
        }
    }
}
