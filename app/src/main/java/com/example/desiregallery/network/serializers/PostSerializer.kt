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

        numOfRates.addProperty("integerValue", src.numOfRates)
        rating.addProperty("doubleValue", src.rating)
        imageUrl.addProperty("stringValue", src.imageUrl.toString())

        with(fields) {
            add("numOfRates", numOfRates)
            add("rating", rating)
            add("imageUrl", imageUrl)
        }
        result.add("fields", fields)
        return result
    }
}