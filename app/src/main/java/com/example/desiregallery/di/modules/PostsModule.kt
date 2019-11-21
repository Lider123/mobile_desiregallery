package com.example.desiregallery.di.modules

import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.ui.screens.post.PostPresenter
import dagger.Module
import dagger.Provides

/**
 * @author babaetskv on 12.11.19
 */
@Module
class PostsModule {

    @Provides
    fun providePresenter(networkManager: NetworkManager): PostPresenter =
        PostPresenter(networkManager)
}
