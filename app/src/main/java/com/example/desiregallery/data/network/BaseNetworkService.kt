package com.example.desiregallery.data.network

import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.models.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*

interface BaseNetworkService {

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

    companion object {
        private const val BASE_URL = "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents/"

        fun createService(): BaseNetworkService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(createBaseGson())
                .build()
            return retrofit.create(BaseNetworkService::class.java)
        }
    }
}
