package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.Comment
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

        if (src.comments.isNotEmpty()) {
            for (c in src.comments) {
                commentsArray.add(serializeComment(c))
            }
            commentsObject.add("values", commentsArray)
        }

        numOfRates.addProperty("integerValue", src.numOfRates)
        rating.addProperty("doubleValue", src.rating)
        imageUrl.addProperty("stringValue", src.imageUrl.toString())
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

    private fun serializeComment(comment: Comment) : JsonObject {
        val commentFields = JsonObject().apply {
            val text = JsonObject()
            text.addProperty("stringValue", comment.text)

            val datetime = JsonObject()
            datetime.addProperty("stringValue", comment.datetime)

            add("text", text)
            add("datetime", datetime)
        }
        val mapValue = JsonObject().apply { add("fields", commentFields) }
        return JsonObject().apply { add("mapValue", mapValue) }
    }
}