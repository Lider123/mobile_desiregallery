package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.FIELDS
import com.example.desiregallery.data.network.INTEGER_VALUE
import com.example.desiregallery.data.network.NAME
import com.example.desiregallery.data.network.STRING_VALUE
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * @author babaetskv on 20.09.19
 */
class CommentDeserializer : JsonDeserializer<Comment> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Comment {
        val rootObject = json.asJsonObject
        val fieldsObject = rootObject.getAsJsonObject(FIELDS)

        val id = rootObject.get(NAME).asString.split("/").last()
        val text = fieldsObject.getAsJsonObject("text").get(STRING_VALUE).asString
        val authorName = fieldsObject.getAsJsonObject("author").get(STRING_VALUE)
            .asString
        val datetime = fieldsObject.getAsJsonObject("timestamp").get(INTEGER_VALUE)
            .asLong
        val postId = fieldsObject.getAsJsonObject("postId").get(STRING_VALUE).asString

        return Comment(
            text = text,
            author = User("", "").apply { login = authorName },
            timestamp = datetime,
            postId = postId
        ).also {
            it.id = id
        }
    }
}
