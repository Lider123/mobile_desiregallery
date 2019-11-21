package com.example.desiregallery.di.modules

import android.app.Application
import android.content.ContentResolver
import android.content.res.Resources
import com.example.desiregallery.MainApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author babaetskv on 12.11.19
 */
@Module
class AppModule(private val app: MainApplication) {

    @Singleton
    @Provides
    fun provideApp(): Application = app

    @Singleton
    @Provides
    fun provideResources(): Resources = app.resources

    @Singleton
    @Provides
    fun provideContentResolver(): ContentResolver = app.contentResolver
}
