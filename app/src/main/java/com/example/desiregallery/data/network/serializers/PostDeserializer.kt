package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.FIELDS
import com.example.desiregallery.data.network.INTEGER_VALUE
import com.example.desiregallery.data.network.NAME
import com.example.desiregallery.data.network.STRING_VALUE
import com.google.gson.*

import java.lang.reflect.Type

class PostDeserializer : JsonDeserializer<Post> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Post {
        val rootObject = json.asJsonObject
        val fieldsObject = rootObject.getAsJsonObject(FIELDS)
        val ratingObject = fieldsObject.getAsJsonObject("rating")

        val id = rootObject.get(NAME).asString.split("/").last()
        val authorName = fieldsObject.getAsJsonObject("author").get(STRING_VALUE).asString
        val imageUrl = fieldsObject.getAsJsonObject("imageUrl").get(STRING_VALUE).asString
        val rating =
            ratingObject.get("doubleValue")?.asFloat ?: ratingObject.get(INTEGER_VALUE).asFloat
        val numOfRates = fieldsObject.getAsJsonObject("numOfRates").get(INTEGER_VALUE).asInt
        val timestamp = fieldsObject.getAsJsonObject("timestamp").get(INTEGER_VALUE).asLong

        return Post(
            author = User("", "").apply { login = authorName },
            rating = rating,
            numOfRates = numOfRates,
            timestamp = timestamp
        ).also {
            it.id = id
            it.setImageUrl(imageUrl)
        }
    }
}
