package com.example.desiregallery.auth

import com.example.desiregallery.models.User

/**
 * @author babaetskv on 17.09.19
 */
class EmailAccount(val user: User) : IAccount {

    override fun getAccessToken(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDisplayName() = user.login

    override fun getPhotoUrl() = user.photo

    override fun getGender() = user.gender

    override fun getBirthday() = user.birthday
}