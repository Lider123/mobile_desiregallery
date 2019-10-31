package com.example.desiregallery.data.network

import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.query.QueryRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author babaetskv on 19.09.19
 */
interface IQueryApi {

    @POST(" ")
    fun getComments(@Body query: QueryRequest): Call<List<Comment>>

    @POST(" ")
    fun getPosts(@Body query: QueryRequest): Call<List<Post>>
}
