package com.example.desiregallery

import android.app.Application
import io.realm.Realm

class MainApplication : Application() {
    companion object {
        const val APP_PREFERENCES = "app_prefs"
        const val PREFS_CURR_USER_KEY = "currUser"

        private var instance: MainApplication? = null

        fun getInstance(): MainApplication {
            if (instance == null)
                instance = MainApplication()
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}