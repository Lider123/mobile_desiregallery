package com.example.desiregallery.di.modules

import android.content.res.Resources
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.auth.IAccount
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.ui.screens.profile.ProfilePresenter
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

/**
 * @author babaetskv on 12.11.19
 */
@Module
class ProfileModule(private val account: IAccount?) {

    @Provides
    fun providePresenter(
        resources: Resources,
        accProvider: AccountProvider,
        networkManager: NetworkManager,
        auth: FirebaseAuth
    ): ProfilePresenter = ProfilePresenter(account, resources, accProvider, networkManager, auth)
}
