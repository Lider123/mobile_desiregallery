package com.example.desiregallery.auth

import java.io.Serializable

/**
 * @author babaetskv on 17.09.19
 */
interface IAccount : Serializable {
    val accessToken: String
    val displayName: String
    val photoUrl: String
    val birthday: String

    fun logOut()
}
