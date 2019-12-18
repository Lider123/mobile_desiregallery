package com.example.desiregallery.di

import com.example.desiregallery.MessagingHelper
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.di.modules.AuthModule
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import io.mockk.mockk

/**
 * @author babaetskv on 09.12.19
 */
class TestAuthModule : AuthModule(mockk()) {

    override fun provideAccountProvider(
        prefs: IDGSharedPreferencesHelper,
        auth: FirebaseAuth,
        messagingHelper: MessagingHelper,
        networkManager: NetworkManager,
        googleClient: GoogleSignInClient
    ): AccountProvider = mockk()
}
