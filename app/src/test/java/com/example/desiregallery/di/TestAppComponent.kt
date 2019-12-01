package com.example.desiregallery.di

import com.example.desiregallery.NetworkManagerUnitTest
import com.example.desiregallery.SharedPreferencesHelperUnitTest
import com.example.desiregallery.di.components.AppComponent
import com.example.desiregallery.di.modules.*
import dagger.Component
import javax.inject.Singleton

/**
 * @author Konstantin on 01.12.2019
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        AuthModule::class,
        AnalyticsModule::class,
        DataModule::class,
        ProfileModule::class,
        PostsModule::class,
        PostCreationModule::class,
        FeedModule::class
    ]
)
interface TestAppComponent : AppComponent {
    fun inject(test: NetworkManagerUnitTest)
    fun inject(test: SharedPreferencesHelperUnitTest)
}
