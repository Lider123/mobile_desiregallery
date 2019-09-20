package com.example.desiregallery.network

import com.example.desiregallery.models.Comment
import com.example.desiregallery.network.query.QueryRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author babaetskv on 19.09.19
 */
interface IQueryApi {

    @POST(" ")
    fun getComments(@Body query: QueryRequest): Call<List<Comment>>
}