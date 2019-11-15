package com.example.desiregallery.di.modules

import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.ui.screens.post.PostPresenter
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
}