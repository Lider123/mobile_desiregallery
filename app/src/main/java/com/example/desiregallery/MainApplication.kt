package com.example.desiregallery

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.example.desiregallery.analytics.AnalyticsTracker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.vk.sdk.VKSdk
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory

class MainApplication : Application() {
    companion object {
        const val STORAGE_URL = "gs://desiregallery-8072a.appspot.com"
        const val STORAGE_POST_IMAGES_DIR = "postImages"
        const val STORAGE_PROFILE_IMAGES_DIR = "profileImages"

        lateinit var instance: MainApplication
            private set
        lateinit var storage: FirebaseStorage
            private set
        lateinit var auth: FirebaseAuth
            private set
        lateinit var analyticsTracker: AnalyticsTracker
            private set
    }

    lateinit var googleSignInClient: GoogleSignInClient
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        analyticsTracker = AnalyticsTracker.getInstance(this)
        Realm.init(this)
        if (!BuildConfig.DEBUG)
            initSentry()
        initCrashlytics()
        VKSdk.initialize(applicationContext)
        initGoogleSignInClient()
    }

    private fun initSentry() {
        Sentry.init(BuildConfig.SENTRY_DSN, AndroidSentryClientFactory(applicationContext))
    }

    private fun initCrashlytics() {
        val core = CrashlyticsCore.Builder()
            .disabled(!resources.getBoolean(R.bool.FIREBASE_ANALYTICS_ENABLED))
            .build()
        val crashlytics = Crashlytics.Builder()
            .core(core)
            .build()
        Fabric.with(this, crashlytics)
    }

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
}