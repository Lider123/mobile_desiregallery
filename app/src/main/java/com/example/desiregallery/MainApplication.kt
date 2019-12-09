package com.example.desiregallery

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.example.desiregallery.auth.IAccount
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.di.components.AppComponent
import com.example.desiregallery.di.components.CommentsComponent
import com.example.desiregallery.di.components.DaggerAppComponent
import com.example.desiregallery.di.components.ProfileComponent
import com.example.desiregallery.di.modules.*
import com.example.desiregallery.utils.logging.ReleaseTree
import com.vk.sdk.VKSdk
import io.fabric.sdk.android.Fabric
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import timber.log.Timber

class MainApplication : Application() {
    private var commentsComponent: CommentsComponent? = null
    private var profileComponent: ProfileComponent? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .authModule(AuthModule(applicationContext))
            .networkModule(NetworkModule())
            .analyticsModule(AnalyticsModule(applicationContext))
            .dataModule(DataModule(applicationContext))
            .postsModule(PostsModule())
            .build()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree()) else {
            Timber.plant(ReleaseTree())
            initSentry()
        }
        initCrashlytics()
        VKSdk.initialize(applicationContext)
    }

    private fun initSentry() =
        Sentry.init(BuildConfig.SENTRY_DSN, AndroidSentryClientFactory(applicationContext))

    private fun initCrashlytics() {
        val core = CrashlyticsCore.Builder()
            .disabled(!resources.getBoolean(R.bool.FIREBASE_ANALYTICS_ENABLED))
            .build()
        val crashlytics = Crashlytics.Builder()
            .core(core)
            .build()
        Fabric.with(this, crashlytics)
    }

    fun plusCommentComponent(post: Post): CommentsComponent {
        if (commentsComponent == null) {
            commentsComponent = appComponent.plusCommentsComponent(CommentsModule(post))
        }
        return commentsComponent as CommentsComponent
    }

    fun clearCommentComponent() {
        commentsComponent = null
    }

    fun plusProfileComponent(account: IAccount?): ProfileComponent {
        if (profileComponent == null) {
            profileComponent = appComponent.plusProfileComponent(ProfileModule(account))
        }
        return profileComponent as ProfileComponent
    }

    fun clearProfileComponent() {
        profileComponent = null
    }

    companion object {
        lateinit var instance: MainApplication
            private set
        lateinit var appComponent: AppComponent
            private set
    }
}
