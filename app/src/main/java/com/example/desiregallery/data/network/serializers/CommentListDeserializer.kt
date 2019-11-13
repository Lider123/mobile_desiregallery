package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.network.DOCUMENT
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * @author babaetskv on 20.09.19
 */
class CommentListDeserializer : JsonDeserializer<List<Comment>> {

    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): List<Comment> {
        val documents = json.asJsonArray.filter { it.asJsonObject.has(DOCUMENT) }
        return documents.map {
            context.deserialize(it.asJsonObject.get(DOCUMENT), Comment::class.java) as Comment
        }
    }
}
