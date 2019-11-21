package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.DOUBLE_VALUE
import com.example.desiregallery.data.network.FIELDS
import com.example.desiregallery.data.network.INTEGER_VALUE
import com.example.desiregallery.data.network.STRING_VALUE
import com.google.gson.*
import java.lang.reflect.Type

class PostSerializer : JsonSerializer<Post> {

    override fun serialize(
        src: Post,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val result = JsonObject()
        val fields = JsonObject()
        val imageUrl = JsonObject()
        val rating = JsonObject()
        val numOfRates = JsonObject()
        val author = JsonObject()
        val timestamp = JsonObject()

        author.addProperty(STRING_VALUE, src.author.login)
        numOfRates.addProperty(INTEGER_VALUE, src.numOfRates)
        rating.addProperty(DOUBLE_VALUE, src.rating)
        imageUrl.addProperty(STRING_VALUE, src.imageUrl.toString())
        timestamp.addProperty(INTEGER_VALUE, src.timestamp)

        with(fields) {
            add("author", author)
            add("numOfRates", numOfRates)
            add("rating", rating)
            add("imageUrl", imageUrl)
            add("timestamp", timestamp)
        }
        result.add(FIELDS, fields)
        return result
    }
}
