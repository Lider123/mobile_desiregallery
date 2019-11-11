package com.example.desiregallery.auth

import com.example.desiregallery.data.models.User
import com.google.firebase.auth.FirebaseAuth

/**
 * @author babaetskv on 17.09.19
 */
class EmailAccount(val user: User, private val auth: FirebaseAuth) : IAccount {
    override val accessToken = "" // TODO
    override val displayName = user.login
    override val photoUrl = user.photo
    override val birthday = user.birthday

    override fun logOut() {
        auth.signOut()
    }
}
