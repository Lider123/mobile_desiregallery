package com.example.desiregallery.network.serializers

import com.example.desiregallery.models.Comment
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * @author babaetskv on 20.09.19
 */
class CommentListDeserializer : JsonDeserializer<List<Comment>> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<Comment> {
        val documents = json.asJsonArray
        if (!json.toString().contains("document"))
            return ArrayList()
        return documents.map { context.deserialize(it.asJsonObject.get("document"), Comment::class.java) as Comment }
    }
}