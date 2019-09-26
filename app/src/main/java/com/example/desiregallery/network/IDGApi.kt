package com.example.desiregallery.network

import com.example.desiregallery.models.Comment
import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User
import retrofit2.Call
import retrofit2.http.*

interface IDGApi {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("posts/{id}")
    fun getPost(@Path("id") id: String): Call<Post>

    @GET("users/{login}")
    fun getUser(@Path("login") login: String): Call<User>

    @POST("users")
    fun createUser(@Query("documentId") login: String, @Body user: User): Call<User>

    @POST("posts")
    fun createPost(@Query("documentId") id: String, @Body post: Post): Call<Post>

    @POST("comments")
    fun createComment(@Query("documentId") id: String, @Body comment: Comment): Call<Comment>

    @PATCH("users/{login}")
    fun updateUser(@Path("login") login: String, @Body user: User): Call<User>

    @PATCH("posts/{id}")
    fun updatePost(@Path("id") id: String, @Body post: Post): Call<Post>
}
