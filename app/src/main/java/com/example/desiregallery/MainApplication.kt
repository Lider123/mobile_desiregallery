package com.example.desiregallery

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import io.realm.Realm

class MainApplication : Application() {
    companion object {
        const val APP_PREFERENCES = "app_prefs"
        const val PREFS_CURR_USER_KEY = "currUser"

        private var instance: MainApplication? = null
        private var analytics: FirebaseAnalytics? = null

        fun getInstance(): MainApplication {
            if (instance == null)
                instance = MainApplication()
            return instance!!
        }

        fun getAnalytics(): FirebaseAnalytics {
            if (analytics == null)
                analytics = FirebaseAnalytics.getInstance(getInstance())
            return analytics!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}