package com.example.desiregallery.di.modules

import android.app.Application
import com.example.desiregallery.MainApplication
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
}