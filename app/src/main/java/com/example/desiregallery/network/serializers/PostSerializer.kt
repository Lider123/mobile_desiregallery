package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.Post
import com.google.gson.*
import java.lang.reflect.Type

class PostSerializer : JsonSerializer<Post> {
    override fun serialize(src: Post, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val result = JsonObject()
        val fields = JsonObject()
        val imageUrl = JsonObject()
        val rating = JsonObject()
        val numOfRates = JsonObject()
        val author = JsonObject()
        val timestamp = JsonObject()

        author.addProperty("stringValue", src.author.login)
        numOfRates.addProperty("integerValue", src.numOfRates)
        rating.addProperty("doubleValue", src.rating)
        imageUrl.addProperty("stringValue", src.imageUrl.toString())
        timestamp.addProperty("integerValue", src.timestamp)

        with(fields) {
            add("author", author)
            add("numOfRates", numOfRates)
            add("rating", rating)
            add("imageUrl", imageUrl)
            add("timestamp", timestamp)
        }
        result.add("fields", fields)
        return result
    }
}