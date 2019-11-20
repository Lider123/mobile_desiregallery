package com.example.desiregallery.data.network

import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.query.requests.CommentsQueryRequest
import com.example.desiregallery.data.network.query.requests.PostsQueryRequest
import com.example.desiregallery.data.network.serializers.CommentDeserializer
import com.example.desiregallery.data.network.serializers.CommentListDeserializer
import com.example.desiregallery.data.network.serializers.PostDeserializer
import com.example.desiregallery.data.network.serializers.PostListDeserializer
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author babaetskv on 19.09.19
 */
interface QueryService {

    @POST(" ")
    fun getComments(@Body query: CommentsQueryRequest): Call<List<Comment>>

    @POST(" ")
    fun getPosts(@Body query: PostsQueryRequest): Call<List<Post>>

    companion object {
        private const val QUERY_URL = "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents:runQuery/"

        private fun createQueryGson(): GsonConverterFactory {
            val postListType = object : TypeToken<MutableList<Post>>() {}.type
            val commentListType = object : TypeToken<MutableList<Comment>>() {}.type
            val gsonBuilder = GsonBuilder()
                .registerTypeAdapter(Comment::class.java, CommentDeserializer())
                .registerTypeAdapter(commentListType, CommentListDeserializer())
                .registerTypeAdapter(Post::class.java, PostDeserializer())
                .registerTypeAdapter(postListType, PostListDeserializer())
            return GsonConverterFactory.create(gsonBuilder.create())
        }

        fun createService(): QueryService {
            val retrofit = Retrofit.Builder()
                .baseUrl(QUERY_URL)
                .addConverterFactory(createQueryGson())
                .build()
            return retrofit.create(QueryService::class.java)
        }
    }
}
