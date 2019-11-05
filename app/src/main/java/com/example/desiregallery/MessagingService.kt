package com.example.desiregallery

import com.example.desiregallery.utils.logDebug
import com.google.firebase.messaging.FirebaseMessagingService

/**
 * @author babaetskv on 05.11.19
 */
class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        logDebug(TAG, "Refreshed token: $token")
        super.onNewToken(token)
    }

    companion object {
        private val TAG = MessagingService::class.java.simpleName
    }
}