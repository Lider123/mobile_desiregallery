package com.example.desiregallery

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.example.desiregallery.di.applicationModule
import com.example.desiregallery.di.networkModule
import com.example.desiregallery.di.viewModelModule
import com.vk.sdk.VKSdk
import io.fabric.sdk.android.Fabric
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            loadKoinModules(listOf(applicationModule, networkModule, viewModelModule))
        }
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
}