package com.example.desiregallery.auth

import com.vk.sdk.VKSdk
import com.vk.sdk.api.model.VKApiUser

/**
 * @author babaetskv on 17.09.19
 */

class VKAccount(private val user: VKApiUser) : IAccount {
    override val accessToken = "" // TODO
    override val displayName = "${user.first_name} ${user.last_name}"
    override val photoUrl: String = user.photo_max
    override val gender: String
        get() {
            val fields = user.fields
            if (!fields.has("sex"))
                return ""

            return when(fields.getInt("sex")) {
                1 -> "female"
                2 -> "male"
                else -> ""
            }
        }
    override val birthday: String
        get() {
            val fields = user.fields
            if (!fields.has("bdate"))
                return ""

            return fields.getString("bdate")
        }

    override fun logOut() {
        VKSdk.logout()
    }
}