package com.example.desiregallery.di.modules

import android.content.Context
import com.example.desiregallery.auth.AccountProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Konstantin on 11.11.2019
 */
@Module
class AuthModule() {

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideAccountProvider(): AccountProvider {
        return AccountProvider()
    }
}
