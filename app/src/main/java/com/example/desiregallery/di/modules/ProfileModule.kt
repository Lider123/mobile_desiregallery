package com.example.desiregallery.di.modules

import com.example.desiregallery.ui.screens.profile.IProfileContract
import com.example.desiregallery.ui.screens.profile.ProfilePresenter
import dagger.Module
import dagger.Provides

/**
 * @author Konstantin on 11.11.2019
 */
@Module
class ProfileModule {

    @Provides
    fun provideProfilePresenter(view: IProfileContract.View): IProfileContract.Presenter {
        return ProfilePresenter(view)
    }
}