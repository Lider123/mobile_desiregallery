package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.Post
import com.google.gson.*

import java.lang.reflect.Type

class PostDeserializer : JsonDeserializer<Post> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Post {
        val rootObject = json.asJsonObject
        val fieldsObject = rootObject.getAsJsonObject("fields")
        val ratingObject = fieldsObject.getAsJsonObject("rating")
        val commentsObject = fieldsObject
            .getAsJsonObject("comments")
            .getAsJsonObject("arrayValue")
            .getAsJsonArray("values")

        val name = rootObject.get("name").asString.split("/")
        val id = name.last()

        val imageUrl = fieldsObject.getAsJsonObject("imageUrl").get("stringValue").asString

        val rating: Float = ratingObject.get("doubleValue")?.asFloat ?: ratingObject.get("integerValue").asFloat

        val numOfRates = fieldsObject.getAsJsonObject("numOfRates").get("integerValue").asInt

        val comments = commentsObject?.map { it.asJsonObject.get("stringValue").asString } ?: emptyList()

        val post = Post()
        post.setId(id)
        post.setImageUrl(imageUrl)
        post.setRating(rating)
        post.setNumOfRates(numOfRates)
        post.setComments(comments)
        return post
    }
}
