package com.example.desiregallery.data.network.serializers

import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.DOCUMENT
import com.example.desiregallery.data.network.NetworkUtils
import com.google.gson.*

import java.lang.reflect.Type
import javax.inject.Inject

class PostListDeserializer : JsonDeserializer<List<Post>> {
    @Inject
    lateinit var networkUtils: NetworkUtils

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type,
                             context: JsonDeserializationContext): List<Post>  {
        val documents = json.asJsonArray.filter { it.asJsonObject.has(DOCUMENT) }
        val posts: List<Post> = documents.map {
            context.deserialize(it.asJsonObject.get(DOCUMENT), Post::class.java) as Post
        }

        val authors: Set<String> = LinkedHashSet(posts.map { post -> post.author.login })
        val users = networkUtils.getUsersByNames(authors)
        posts.map { post ->
            val authorName = post.author.login
            post.author = users.find { user -> user.login == authorName } as User
        }
        return posts
    }
}
