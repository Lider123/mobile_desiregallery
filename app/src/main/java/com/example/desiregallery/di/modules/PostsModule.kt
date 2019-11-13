package com.example.desiregallery.di.modules

import android.app.Application
import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.data.network.NetworkUtils
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.ui.presenters.PostPresenter
import com.example.desiregallery.ui.screens.feed.PostsViewModel
import dagger.Module
import dagger.Provides

/**
 * @author babaetskv on 12.11.19
 */
@Module
class PostsModule {

    @Provides
    fun providePresenter(networkService: BaseNetworkService): PostPresenter {
        return PostPresenter(networkService)
    }

    @Provides
    fun provideViewModelFactory(app: Application,
                                baseService:
                                BaseNetworkService,
                                queryService: QueryNetworkService,
                                networkUtils: NetworkUtils): PostsViewModel.Factory {
        return PostsViewModel.Factory(app, baseService, queryService, networkUtils)
    }
}