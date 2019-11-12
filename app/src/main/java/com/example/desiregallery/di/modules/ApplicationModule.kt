package com.example.desiregallery.di.modules

import android.app.Application
import android.content.res.Resources
import com.example.desiregallery.MainApplication
import com.example.desiregallery.analytics.AnalyticsTracker
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.data.prefs.PreferencesHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Konstantin on 11.11.2019
 */
@Module
class ApplicationModule(private val app: MainApplication) {

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return app
    }

    @Singleton
    @Provides
    fun provideResources(): Resources {
        return app.resources
    }

    @Singleton
    @Provides
    fun providePrefsHelper(): IDGSharedPreferencesHelper {
        return PreferencesHelper(app.applicationContext)
    }

    @Singleton
    @Provides
    fun provideAnalyticsTracker(): IDGAnalyticsTracker {
        return AnalyticsTracker(app.applicationContext)
    }

    @Singleton
    @Provides
    fun provideGoogleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(app.applicationContext, gso)
    }
}
