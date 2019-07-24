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
        val comments = JsonObject()
        val commentsObject = JsonObject()
        val commentsArray = JsonArray()

        if (src.getComments().isNotEmpty()) {
            for (c in src.getComments()) {
                commentsArray.add(JsonObject().apply { addProperty("stringValue", c) })
            }
            commentsObject.add("values", commentsArray)
        }

        numOfRates.addProperty("integerValue", src.getNumOfRates())
        rating.addProperty("doubleValue", src.getRating())
        imageUrl.addProperty("stringValue", src.getImageUrl().toString())
        comments.add("arrayValue", commentsObject)

        with(fields) {
            add("numOfRates", numOfRates)
            add("rating", rating)
            add("imageUrl", imageUrl)
            add("comments", comments)
        }
        result.add("fields", fields)
        return result
    }
}