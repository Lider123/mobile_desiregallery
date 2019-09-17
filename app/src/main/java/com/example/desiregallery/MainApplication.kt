package com.example.desiregallery

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.vk.sdk.VKSdk
import io.realm.Realm

class MainApplication : Application() {
    companion object {
        const val STORAGE_URL = "gs://desiregallery-8072a.appspot.com"
        const val STORAGE_POST_IMAGES_DIR = "postImages"
        const val STORAGE_PROFILE_IMAGES_DIR = "profileImages"

        private var instance: MainApplication? = null
        private var analytics: FirebaseAnalytics? = null
        private var storage: FirebaseStorage? = null
        private var auth: FirebaseAuth? = null

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

        fun getAuth(): FirebaseAuth {
            if (auth == null)
                auth = FirebaseAuth.getInstance()
            return auth!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        VKSdk.initialize(applicationContext)
    }
}