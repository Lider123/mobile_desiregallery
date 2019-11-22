package com.example.desiregallery.di.modules

import android.app.Application
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.di.scopes.CommentsScope
import com.example.desiregallery.ui.screens.comments.CommentsViewModel
import dagger.Module
import dagger.Provides

/**
 * @author babaetskv on 12.11.19
 */
@Module
class CommentsModule(private val postId: String) {

    @CommentsScope
    @Provides
    fun provideViewModelFactory(
        app: Application,
        networkManager: NetworkManager
    ): CommentsViewModel.Factory = CommentsViewModel.Factory(postId, app, networkManager)
}
