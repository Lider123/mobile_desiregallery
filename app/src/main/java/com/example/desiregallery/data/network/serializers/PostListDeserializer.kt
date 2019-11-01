package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.DOCUMENT
import com.example.desiregallery.data.network.NetworkHelper
import com.google.gson.*
import org.koin.core.KoinComponent
import org.koin.core.inject

import java.lang.reflect.Type

class PostListDeserializer : JsonDeserializer<List<Post>>, KoinComponent {
    private val networkHelper: NetworkHelper by inject()

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): List<Post>  {
        val documents = json.asJsonArray.filter { it.asJsonObject.has(DOCUMENT) }
        val posts: List<Post> = documents.map {
            context.deserialize(it.asJsonObject.get(DOCUMENT), Post::class.java) as Post
        }

        val authors: Set<String> = LinkedHashSet(posts.map { post -> post.author.login })
        val users = networkHelper.getUsersByNames(authors)
        posts.map { post ->
            val authorName = post.author.login
            post.author = users.find { user -> user.login == authorName } as User
        }
        return posts
    }
}
