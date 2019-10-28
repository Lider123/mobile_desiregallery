package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.Comment
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/**
 * @author babaetskv on 20.09.19
 */
class CommentSerializer : JsonSerializer<Comment> {

    override fun serialize(src: Comment, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val result = JsonObject()
        val fields = JsonObject()
        val text = JsonObject()
        val author = JsonObject()
        val datetime = JsonObject()
        val postId = JsonObject()

        text.addProperty("stringValue", src.text)
        author.addProperty("stringValue", src.author.login)
        datetime.addProperty("stringValue", src.datetime.toString())
        postId.addProperty("stringValue", src.postId)

        with(fields) {
            add("text", text)
            add("author", author)
            add("datetime", datetime)
            add("postId", postId)
        }
        result.add("fields", fields)
        return result
    }
}