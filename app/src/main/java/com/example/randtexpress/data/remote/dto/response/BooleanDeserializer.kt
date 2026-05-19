package com.example.randtexpress.data.remote.dto.response

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class BooleanDeserializer : TypeAdapter<Boolean>() {
    override fun write(out: JsonWriter, value: Boolean?) {
        out.value(value ?: false)
    }

    override fun read(reader: JsonReader): Boolean {
        return when (reader.peek()) {
            JsonToken.BOOLEAN -> reader.nextBoolean()
            JsonToken.NUMBER -> reader.nextInt() != 0
            JsonToken.STRING -> {
                val value = reader.nextString().lowercase()
                value == "true" || value == "1"
            }
            JsonToken.NULL -> {
                reader.nextNull()
                false
            }
            else -> {
                reader.skipValue()
                false
            }
        }
    }
}
