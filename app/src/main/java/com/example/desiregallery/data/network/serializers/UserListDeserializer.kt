package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.DOCUMENTS
import com.google.gson.*

import java.lang.reflect.Type

class UserListDeserializer : JsonDeserializer<List<User>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<User> {
        val documents = json.asJsonObject.get(DOCUMENTS).asJsonArray
        return documents.map { context.deserialize(it, User::class.java) as User }
    }
}
