package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.Notification
import com.example.desiregallery.data.network.FIELDS
import com.example.desiregallery.data.network.STRING_VALUE
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/**
 * @author babaetskv on 25.11.19
 */
class NotificationSerializer : JsonSerializer<Notification> {

    override fun serialize(
        src: Notification,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val result = JsonObject()
        val fields = JsonObject()
        val id = JsonObject()
        val loginTo = JsonObject()
        val message = JsonObject()

        id.addProperty(STRING_VALUE, src.id)
        loginTo.addProperty(STRING_VALUE, src.loginTo)
        message.addProperty(STRING_VALUE, src.message)

        with(fields) {
            add("id", id)
            add("loginTo", loginTo)
            add("message", message)
        }
        result.add(FIELDS, fields)
        return result
    }
}
