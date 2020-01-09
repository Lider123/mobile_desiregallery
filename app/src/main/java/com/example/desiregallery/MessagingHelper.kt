package com.example.desiregallery

import com.example.desiregallery.data.Result
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * @author Konstantin on 22.11.2019
 */
class MessagingHelper {

    suspend fun fetchMessageToken(): Result<String> = withContext(Dispatchers.IO) {
        val task = FirebaseInstanceId.getInstance().instanceId
        task.await()
        return@withContext if (task.isSuccessful) Result.Success(task.result!!.token) else {
            Result.Error(task.exception!!)
        }
    }
}
