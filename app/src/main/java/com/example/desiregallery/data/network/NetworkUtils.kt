@file:JvmName("DGNetwork")
package com.example.desiregallery.data.network

import com.example.desiregallery.data.models.Post
import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.data.models.User
import com.example.desiregallery.utils.logError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class NetworkUtils(private val networkService: BaseNetworkService) {

    fun getUsersByNames(userNames: Collection<String>): List<User> {
        logInfo(TAG, "Preparing to load ${userNames.size} users")
        val users = ArrayList<User>()
        val executor = Executors.newCachedThreadPool()
        for (name in userNames)
            executor.execute {
                val userResponse = networkService.getUser(name).execute()
                if (!userResponse.isSuccessful || userResponse.body() == null)
                    throw Exception("There was an error while fetching users")

                users.add(userResponse.body()!!)
            }
        executor.shutdown()
        executor.awaitTermination(15, TimeUnit.SECONDS)
        logInfo(TAG, "Loaded ${users.size} users")
        if (users.size != userNames.size)
            throw Exception("There was an error while fetching users")
        return users
    }

    fun saveUserInfo(user: User) {
        networkService.updateUser(user.login, user).enqueue(object: Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {
                logInfo(TAG, "Data of user ${user.login} have successfully been saved to firestore")
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                logError(TAG, "Unable to save user data to firestore: ${t.message}")
            }
        })
    }

    fun uploadPost(post: Post, callback: UploadCallback) {
        post.timestamp = Date().time
        networkService.createPost(post.id, post).enqueue(object: Callback<Post> {

            override fun onFailure(call: Call<Post>, t: Throwable) {
                logError(TAG, "Unable to create post ${post.id}: ${t.message}")
                callback.onFailure()
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful)
                    logError(TAG, "Unable to create post ${post.id}: received response with code ${response.code()}")
                else
                    logInfo(TAG, "Post ${post.id} successfully created")
                callback.onComplete()
            }
        })
    }

    companion object {
        private val TAG = NetworkUtils::class.java.simpleName
    }
}

interface UploadCallback {
    fun onComplete()
    fun onFailure()
}
