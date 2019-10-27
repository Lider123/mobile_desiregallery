@file:JvmName("DGNetwork")
package com.example.desiregallery.network

import com.example.desiregallery.logging.logInfo
import com.example.desiregallery.models.User
import retrofit2.Retrofit
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private const val TAG = "DGNetwork"
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

fun getUsersByNames(userNames: Collection<String>): List<User> {
    logInfo(TAG, "Preparing to load ${userNames.size} users")
    val users = ArrayList<User>()
    val executor = Executors.newCachedThreadPool()
    for (name in userNames)
        executor.execute {
            val userResponse = baseService.getUser(name).execute()
            if (!userResponse.isSuccessful || userResponse.body() == null)
                throw Exception("There was an error while fetching authors")

            users.add(userResponse.body()!!)
        }
    executor.shutdown()
    executor.awaitTermination(15, TimeUnit.SECONDS)
    logInfo(TAG, "Loaded ${users.size} users")
    if (users.size != userNames.size)
        throw Exception("There was an error while fetching authors")
    return users
}
