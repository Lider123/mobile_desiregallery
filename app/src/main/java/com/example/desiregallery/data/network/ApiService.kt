package com.example.desiregallery.data.network

import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.Notification
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.serializers.*
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

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

    @PATCH("notifications/{loginTo}")
    fun updateNotification(@Path("loginTo") loginTo: String, @Body notification: Notification): Call<Notification>

    companion object {
        private const val BASE_URL =
            "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents/"

        private fun createApiGson(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder()
                .registerTypeAdapter(Post::class.java, PostDeserializer())
                .registerTypeAdapter(Post::class.java, PostSerializer())
                .registerTypeAdapter(User::class.java, UserDeserializer())
                .registerTypeAdapter(User::class.java, UserSerializer())
                .registerTypeAdapter(Comment::class.java, CommentSerializer())
                .registerTypeAdapter(Comment::class.java, CommentDeserializer())
                .registerTypeAdapter(Notification::class.java, NotificationSerializer())
            return GsonConverterFactory.create(gsonBuilder.create())
        }

        fun createService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(createApiGson())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
