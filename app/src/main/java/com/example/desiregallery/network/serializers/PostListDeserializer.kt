package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.Post
import com.google.gson.*

import java.lang.reflect.Type

class PostListDeserializer : JsonDeserializer<List<Post>> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<Post>  {
        val documents = json.asJsonObject.get("documents").asJsonArray
        return documents.map { context.deserialize(it, Post::class.java) as Post }
    }
}
