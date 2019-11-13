package com.example.desiregallery.di.modules

import android.content.res.Resources
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.network.QueryNetworkService
import com.example.desiregallery.ui.screens.profile.ProfilePresenter
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

/**
 * @author babaetskv on 12.11.19
 */
@Module
class ProfileModule {

    @Provides
    fun providePresenter(resources: Resources, accProvider: AccountProvider, queryService: QueryNetworkService, auth: FirebaseAuth): ProfilePresenter {
        return ProfilePresenter(resources, accProvider, queryService, auth)
    }
}