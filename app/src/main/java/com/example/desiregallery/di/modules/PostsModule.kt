package com.example.desiregallery.di.modules

import com.example.desiregallery.data.models.Post
import com.example.desiregallery.ui.presenters.IPostContract
import com.example.desiregallery.ui.presenters.PostPresenter
import dagger.Module
import dagger.Provides

/**
 * @author Konstantin on 11.11.2019
 */
@Module
class PostsModule {

    @Provides
    fun providePostPresenter(view: IPostContract.View, post: Post): IPostContract.Presenter {
        return PostPresenter(view, post)
    }
}