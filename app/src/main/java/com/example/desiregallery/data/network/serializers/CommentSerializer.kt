package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.network.FIELDS
import com.example.desiregallery.data.network.INTEGER_VALUE
import com.example.desiregallery.data.network.STRING_VALUE
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/**
 * @author babaetskv on 20.09.19
 */
class CommentSerializer : JsonSerializer<Comment> {

    override fun serialize(
        src: Comment,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val result = JsonObject()
        val fields = JsonObject()
        val text = JsonObject()
        val author = JsonObject()
        val datetime = JsonObject()
        val postId = JsonObject()

        text.addProperty(STRING_VALUE, src.text)
        author.addProperty(STRING_VALUE, src.author.login)
        datetime.addProperty(INTEGER_VALUE, src.timestamp)
        postId.addProperty(STRING_VALUE, src.postId)

        with(fields) {
            add("text", text)
            add("author", author)
            add("timestamp", datetime)
            add("postId", postId)
        }
        result.add(FIELDS, fields)
        return result
    }
}
