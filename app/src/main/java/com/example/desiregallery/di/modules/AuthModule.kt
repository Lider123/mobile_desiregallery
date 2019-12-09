package com.example.desiregallery.di.modules

import android.content.Context
import com.example.desiregallery.MessagingHelper
import com.example.desiregallery.auth.AccountProvider
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author babaetskv on 12.11.19
 */
@Module
class AuthModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideAccountProvider(
        prefs: IDGSharedPreferencesHelper,
        auth: FirebaseAuth,
        messagingHelper: MessagingHelper,
        networkManager: NetworkManager,
        googleClient: GoogleSignInClient
    ): AccountProvider =
        AccountProvider(context, prefs, auth, messagingHelper, networkManager, googleClient)

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideGoogleClient(): GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, options)
    }
}
