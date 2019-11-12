package com.example.desiregallery

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.example.desiregallery.di.components.ApplicationComponent
import com.example.desiregallery.di.modules.*
import com.vk.sdk.VKSdk
import io.fabric.sdk.android.Fabric
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        /*component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .authModule(AuthModule())
            .dataModule(DataModule())
            .networkModule(NetworkModule())
            .postsModule(PostsModule())
            .profileModule(ProfileModule())
            .build()*/

        if (!BuildConfig.DEBUG)
            initSentry()
        initCrashlytics()
        VKSdk.initialize(applicationContext)
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

    companion object {
        lateinit var instance: MainApplication
            private set
        lateinit var component: ApplicationComponent
            private set
    }
}