package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.Comment
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * @author babaetskv on 20.09.19
 */
class CommentDeserializer : JsonDeserializer<Comment> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Comment {
        val rootObject = json.asJsonObject
        val fieldsObject = rootObject.getAsJsonObject("fields")

        val id = rootObject.get("name").asString.split("/").last()

        val text = fieldsObject.getAsJsonObject("text").get("stringValue").asString

        val author = fieldsObject.getAsJsonObject("author").get("stringValue").asString

        val datetime = fieldsObject.getAsJsonObject("datetime").get("stringValue").asLong

        val postId = fieldsObject.getAsJsonObject("postId").get("stringValue").asString

        return Comment().also {
            it.id = id
            it.text = text
            it.author = author
            it.datetime = datetime
            it.postId = postId
        }
    }
}