package com.example.desiregallery.di.modules

import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.data.network.NetworkUtils
import com.example.desiregallery.data.network.QueryNetworkService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Konstantin on 11.11.2019
 */
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideBaseService(): BaseNetworkService {
        return BaseNetworkService.createService()
    }

    @Singleton
    @Provides
    fun provideQueryService(): QueryNetworkService {
        return QueryNetworkService.getService()
    }

    @Singleton
    @Provides
    fun provideNetworkUtils() : NetworkUtils {
        return NetworkUtils()
    }
}