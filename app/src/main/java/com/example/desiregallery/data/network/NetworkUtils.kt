@file:JvmName("DGNetwork")
package com.example.desiregallery.data.network

import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.data.models.User
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class NetworkUtils : KoinComponent {
    private val networkService: BaseNetworkService by inject()

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

    companion object {
        private val TAG = NetworkUtils::class.java.simpleName
    }
}
