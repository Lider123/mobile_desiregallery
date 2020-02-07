package com.example.desiregallery

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

/**
 * @author Konstantin on 22.11.2019
 */
class DGMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.i("Got new token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.i("Message received")
        remoteMessage.notification?.let {
            val notification = NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(it.body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build()
            NotificationManagerCompat.from(applicationContext).notify(0, notification)
        }
    }
}
