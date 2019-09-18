package com.example.desiregallery.auth

import com.example.desiregallery.MainApplication
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 * @author babaetskv on 18.09.19
 */
class GoogleAccount(val account: GoogleSignInAccount) : IAccount {
    override val accessToken = "" // TODO
    override val displayName = account.displayName as String
    override val photoUrl = account.photoUrl?.toString()?: ""
    override val gender = ""
    override val birthday = ""

    override fun logOut() {
        MainApplication.instance.googleSignInClient.signOut()
    }
}