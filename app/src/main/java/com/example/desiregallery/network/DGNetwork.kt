package com.example.desiregallery.network

import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User
import com.example.desiregallery.network.serializers.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DGNetwork {
    private const val BASE_URL = "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents/"
    private const val QUERY_URL = "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents:runQuery/"

    private var baseService: IDGApi? = null
    private var queryService: IQueryApi? = null
    private var gson: Gson? = null

    fun getBaseService(): IDGApi {
        baseService?: run { initBaseService() }
        return baseService!!
    }

    fun getQueryService(): IQueryApi {
        queryService?: run { initQueryService() }
        return queryService!!
    }

    private fun getGson(): Gson {
        gson?: run {
            val postListType = object : TypeToken<MutableList<Post>>() {}.type
            val userListType = object : TypeToken<MutableList<User>>() {}.type
            val gsonBuilder = GsonBuilder()
                .registerTypeAdapter(Post::class.java, PostDeserializer())
                .registerTypeAdapter(Post::class.java, PostSerializer())
                .registerTypeAdapter(postListType, PostListDeserializer())
                .registerTypeAdapter(User::class.java, UserDeserializer())
                .registerTypeAdapter(User::class.java, UserSerializer())
                .registerTypeAdapter(userListType, UserListDeserializer())
            gson = gsonBuilder.create()
        }
        return gson!!
    }

    private fun initBaseService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .build()
        baseService = retrofit.create(IDGApi::class.java)
    }

    private fun initQueryService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(QUERY_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        queryService = retrofit.create(IQueryApi::class.java)
    }
}
