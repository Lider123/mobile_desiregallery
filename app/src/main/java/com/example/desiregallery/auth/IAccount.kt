package com.example.desiregallery.auth

/**
 * @author babaetskv on 17.09.19
 */
interface IAccount {
    val accessToken: String
    val displayName: String
    val photoUrl: String
    val birthday: String

    fun logOut()
}
