package com.example.desiregallery.network

import com.example.desiregallery.models.User
import com.example.desiregallery.network.query.QueryBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author babaetskv on 19.09.19
 */
interface IQueryApi {

    @POST(":runQuery")
    fun getUsers(@Body query: QueryBody): Call<List<User>>
}