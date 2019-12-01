package com.example.desiregallery.di

import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.di.modules.AnalyticsModule
import io.mockk.mockk

/**
 * @author babaetskv on 09.12.19
 */
class TestAnalyticsModule : AnalyticsModule(mockk()) {

    override fun provideAnalytics(): IDGAnalyticsTracker = mockk()
}