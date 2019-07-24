package com.example.desiregallery

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.storage.FirebaseStorage
import io.realm.Realm

class MainApplication : Application() {
    companion object {
        const val APP_PREFERENCES = "app_prefs"
        const val PREFS_CURR_USER_KEY = "currUser"
        const val STORAGE_URL = "gs://desiregallery-8072a.appspot.com"
        const val STORAGE_POST_IMAGES_DIR = "postImages"

        private var instance: MainApplication? = null
        private var analytics: FirebaseAnalytics? = null
        private var storage: FirebaseStorage? = null

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

        fun getStorage(): FirebaseStorage {
            if (storage == null)
                storage = FirebaseStorage.getInstance()
            return storage!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}