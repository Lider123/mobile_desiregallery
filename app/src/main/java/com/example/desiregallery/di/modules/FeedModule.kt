package com.example.desiregallery.di.modules

import android.app.Application
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.ui.screens.feed.FeedViewModel
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
        networkManager: NetworkManager
    ): FeedViewModel.Factory = FeedViewModel.Factory(app, networkManager)
}
