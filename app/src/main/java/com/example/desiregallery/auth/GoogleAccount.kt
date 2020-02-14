package com.example.desiregallery.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient

/**
 * @author babaetskv on 18.09.19
 */
class GoogleAccount(account: GoogleSignInAccount, private val client: GoogleSignInClient) :
    IAccount {
    override val accessToken: String
        get() = "" // TODO
    override val displayName = account.displayName as String
    override val photoUrl = account.photoUrl?.toString() ?: ""
    override val birthday: String
        get() = "" // TODO

    override fun logOut() {
        client.signOut()
    }
}
