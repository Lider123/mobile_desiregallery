package com.example.desiregallery.auth

/**
 * @author babaetskv on 17.09.19
 */
interface IAccount {
    fun getAccessToken(): String
    fun getDisplayName(): String
    fun getPhotoUrl(): String
    fun getGender(): String
    fun getBirthday(): String
}