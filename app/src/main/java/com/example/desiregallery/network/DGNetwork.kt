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
    private const val BASE_API = "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents/"

    private var service: IDGApi? = null
    private var gson: Gson? = null

    fun getService(): IDGApi {
        service?: run { initService() }
        return service!!
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

    private fun initService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_API)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .build()
        service = retrofit.create(IDGApi::class.java)
    }
}
