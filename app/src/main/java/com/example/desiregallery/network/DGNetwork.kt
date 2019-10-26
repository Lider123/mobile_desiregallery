@file:JvmName("DGNetwork")
package com.example.desiregallery.network

import retrofit2.Retrofit

private const val BASE_URL = "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents/"
private const val QUERY_URL = "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents:runQuery/"

val baseService: IDGApi by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(createBaseGson())
        .build()
    retrofit.create(IDGApi::class.java)
}
val queryService: IQueryApi  by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl(QUERY_URL)
        .addConverterFactory(createQueryGson())
        .build()
    retrofit.create(IQueryApi::class.java)
}
