package com.example.desiregallery

import com.example.desiregallery.utils.logInfo
import com.example.desiregallery.utils.logWarning
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

/**
 * @author babaetskv on 05.11.19
 */

class MessagingHelper {
    var token: String? = null
        private set

    fun fetchToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                logWarning(TAG, "getInstanceId failed: ${task.exception}")
                return@OnCompleteListener
            }

            token = task.result?.token
            logInfo(TAG, "getInstanceId succeeded: $token")
        })
    }

    companion object {
        private val TAG = MessagingHelper::class.java.simpleName
    }
}