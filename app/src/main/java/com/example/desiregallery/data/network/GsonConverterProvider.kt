@file:JvmName("GsonConverterProvider")
package com.example.desiregallery.data.network

import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.serializers.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author babaetskv on 20.09.19
 */
internal fun createBaseGson(): GsonConverterFactory {
    val postListType = object : TypeToken<MutableList<Post>>() {}.type
    val userListType = object : TypeToken<MutableList<User>>() {}.type
    val commentListType = object : TypeToken<MutableList<Comment>>() {}.type
    val gsonBuilder = GsonBuilder()
        .registerTypeAdapter(Post::class.java, PostDeserializer())
        .registerTypeAdapter(Post::class.java, PostSerializer())
        .registerTypeAdapter(postListType, PostListDeserializer())
        .registerTypeAdapter(User::class.java, UserDeserializer())
        .registerTypeAdapter(User::class.java, UserSerializer())
        .registerTypeAdapter(userListType, UserListDeserializer())
        .registerTypeAdapter(Comment::class.java, CommentSerializer())
        .registerTypeAdapter(Comment::class.java, CommentDeserializer())
        .registerTypeAdapter(commentListType, CommentListDeserializer())
    return GsonConverterFactory.create(gsonBuilder.create())
}

internal fun createQueryGson(): GsonConverterFactory {
    val postListType = object : TypeToken<MutableList<Post>>() {}.type
    val commentListType = object : TypeToken<MutableList<Comment>>() {}.type
    val gsonBuilder = GsonBuilder()
        .registerTypeAdapter(Comment::class.java, CommentSerializer())
        .registerTypeAdapter(Comment::class.java, CommentDeserializer())
        .registerTypeAdapter(commentListType, CommentListDeserializer())
        .registerTypeAdapter(Post::class.java, PostDeserializer())
        .registerTypeAdapter(Post::class.java, PostSerializer())
        .registerTypeAdapter(postListType, PostListDeserializer())
    return GsonConverterFactory.create(gsonBuilder.create())
}
