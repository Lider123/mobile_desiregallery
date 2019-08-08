package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.Comment
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

        val id = rootObject.get("name").asString.split("/").last()

        val imageUrl = fieldsObject.getAsJsonObject("imageUrl").get("stringValue").asString

        val rating: Float = ratingObject.get("doubleValue")?.asFloat ?: ratingObject.get("integerValue").asFloat

        val numOfRates = fieldsObject.getAsJsonObject("numOfRates").get("integerValue").asInt

        val comments = deserializeComments(commentsObject)

        return Post().also {
            it.id = id
            it.setImageUrl(imageUrl)
            it.rating = rating
            it.numOfRates = numOfRates
            it.comments = comments
        }
    }

    private fun deserializeComments(jsonComments: JsonArray?): List<Comment> {
        val comments = ArrayList<Comment>()
        jsonComments?.let {
            for (element in jsonComments) {
                comments.add(deserializeComment(element.asJsonObject))
            }
        }
        return comments
    }

    private fun deserializeComment(jsonComment: JsonObject): Comment {
        val fields = jsonComment.getAsJsonObject("mapValue").getAsJsonObject("fields").asJsonObject
        return Comment(
            fields.getAsJsonObject("text").get("stringValue").asString,
            fields.getAsJsonObject("datetime").get("stringValue").asLong)
    }
}
