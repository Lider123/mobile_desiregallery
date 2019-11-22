package com.example.desiregallery.di.modules

import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.storage.IStorageHelper
import com.example.desiregallery.ui.screens.postcreation.PostCreationPresenter
import dagger.Module
import dagger.Provides

/**
 * @author babaetskv on 18.11.19
 */
@Module
class PostCreationModule {

    @Provides
    fun providePresenter(
        accProvider: AccountProvider,
        storageHelper: IStorageHelper
    ): PostCreationPresenter = PostCreationPresenter(accProvider, storageHelper)
}
