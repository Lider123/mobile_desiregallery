package com.example.desiregallery.auth

import com.example.desiregallery.MainApplication
import com.example.desiregallery.models.User

/**
 * @author babaetskv on 17.09.19
 */
class EmailAccount(val user: User) : IAccount {
    override val accessToken = "" // TODO
    override val displayName = user.login
    override val photoUrl = user.photo
    override val gender = user.gender
    override val birthday = user.birthday

    override fun logOut() {
        MainApplication.auth.signOut()
    }
}