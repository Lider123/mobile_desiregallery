package com.example.desiregallery.di.modules

import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.data.network.NetworkUtils
import com.example.desiregallery.data.network.QueryNetworkService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author babaetskv on 12.11.19
 */
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideBaseNetworkService(): BaseNetworkService {
        return BaseNetworkService.createService()
    }

    @Singleton
    @Provides
    fun provideQueryNetworkService(): QueryNetworkService {
        return QueryNetworkService.createService()
    }

    @Singleton
    @Provides
    fun provideNetworkUtils(): NetworkUtils {
        return NetworkUtils(provideBaseNetworkService())
    }
}