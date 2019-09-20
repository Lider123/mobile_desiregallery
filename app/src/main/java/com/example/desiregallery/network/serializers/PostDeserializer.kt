package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User
import com.google.gson.*

import java.lang.reflect.Type

class PostDeserializer : JsonDeserializer<Post> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Post {
        val rootObject = json.asJsonObject
        val fieldsObject = rootObject.getAsJsonObject("fields")
        val ratingObject = fieldsObject.getAsJsonObject("rating")

        val id = rootObject.get("name").asString.split("/").last()

        val authorName = fieldsObject.getAsJsonObject("author").get("stringValue").asString

        val imageUrl = fieldsObject.getAsJsonObject("imageUrl").get("stringValue").asString

        val rating: Float = ratingObject.get("doubleValue")?.asFloat ?: ratingObject.get("integerValue").asFloat

        val numOfRates = fieldsObject.getAsJsonObject("numOfRates").get("integerValue").asInt

        return Post().also {
            it.id = id
            it.author = User("", "").apply { login = authorName }
            it.setImageUrl(imageUrl)
            it.rating = rating
            it.numOfRates = numOfRates
        }
    }
}
