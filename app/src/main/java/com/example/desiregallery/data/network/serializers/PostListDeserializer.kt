package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.DOCUMENT
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class PostListDeserializer : JsonDeserializer<List<Post>> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<Post> {
        val documents = json.asJsonArray.filter { it.asJsonObject.has(DOCUMENT) }
        return documents.map {
            context.deserialize(it.asJsonObject.get(DOCUMENT), Post::class.java) as Post
        }
    }
}
