package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.User
import com.google.gson.*

import java.lang.reflect.Type

/**
 * @author babaetskv
 * */
class UserListDeserializer : JsonDeserializer<List<User>> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement , typeOfT: Type, context: JsonDeserializationContext): List<User> {
        val documents = json.asJsonObject.get("documents").asJsonArray
        return documents.map { context.deserialize(it, User::class.java) as User }
    }
}
