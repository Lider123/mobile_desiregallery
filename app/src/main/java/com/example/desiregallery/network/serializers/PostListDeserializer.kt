package com.example.desiregallery.network.serializers

import com.example.desiregallery.logging.DGLogger
import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User
import com.example.desiregallery.network.DGNetwork
import com.google.gson.*
import java.lang.Exception

import java.lang.reflect.Type
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class PostListDeserializer : JsonDeserializer<List<Post>> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<Post>  {
        val documents = json.asJsonObject.get("documents").asJsonArray
        val posts = documents.map { context.deserialize(it, Post::class.java) as Post }

        val authors: Set<String> = LinkedHashSet(posts.map { post -> post.author.login })
        DGLogger.logInfo(TAG, "Preparing to load ${authors.size} users")
        val users = ArrayList<User>()
        val executor = Executors.newCachedThreadPool()
        for (author in authors)
            executor.execute {
                val userResponse = DGNetwork.getBaseService().getUser(author).execute()
                if (!userResponse.isSuccessful || userResponse.body() == null)
                    throw Exception("There was an error while fetching authors")

                users.add(userResponse.body()!!)
            }
        executor.shutdown()
        executor.awaitTermination(15, TimeUnit.SECONDS)
        DGLogger.logInfo(TAG, "Loaded ${users.size} users")
        if (users.size != authors.size)
            throw Exception("There was an error while fetching authors")

        posts.map { post ->
            val authorName = post.author.login
            post.author = users.find { user -> user.login == authorName } as User
        }
        return posts
    }

    companion object {
        private val TAG = PostDeserializer::class.java.simpleName
    }
}
