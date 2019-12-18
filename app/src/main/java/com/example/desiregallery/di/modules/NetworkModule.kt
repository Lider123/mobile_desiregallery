package com.example.desiregallery.di.modules

import com.example.desiregallery.data.network.ApiService
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.network.QueryService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author babaetskv on 12.11.19
 */
@Module
open class NetworkModule {

    @Singleton
    @Provides
    open fun provideNetworkManager(): NetworkManager =
        NetworkManager(ApiService.createService(), QueryService.createService())
}
