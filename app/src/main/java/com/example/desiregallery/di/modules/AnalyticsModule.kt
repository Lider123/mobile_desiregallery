package com.example.desiregallery.di.modules

import android.content.Context
import com.example.desiregallery.analytics.AnalyticsTracker
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author babaetskv on 12.11.19
 */
@Module
class AnalyticsModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideAnalytics(): IDGAnalyticsTracker = AnalyticsTracker(context)
}
