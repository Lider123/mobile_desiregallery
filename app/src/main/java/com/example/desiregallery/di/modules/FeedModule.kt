package com.example.desiregallery.di.modules

import android.app.Application
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.ui.screens.feed.PostsRepository
import com.example.desiregallery.ui.screens.feed.PostsViewModel
import dagger.Module
import dagger.Provides

/**
 * @author babaetskv on 18.11.19
 */
@Module
class FeedModule {

    @Provides
    fun provideViewModelFactory(
        app: Application,
        repository: PostsRepository
    ): PostsViewModel.Factory = PostsViewModel.Factory(app, repository)

    @Provides
    fun providePostsRepository(networkManager: NetworkManager): PostsRepository =
        PostsRepository(networkManager)
}
