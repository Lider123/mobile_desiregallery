package com.example.desiregallery.auth

import android.content.res.Resources
import com.example.desiregallery.R
import com.vk.sdk.VKSdk
import com.vk.sdk.api.model.VKApiUser

/**
 * @author babaetskv on 17.09.19
 */

class VKAccount(resources: Resources, private val user: VKApiUser) : IAccount {
    private val GENDER_MALE = resources.getString(R.string.male)
    private val GENDER_FEMALE = resources.getString(R.string.female)

    override val accessToken = "" // TODO
    override val displayName = "${user.first_name} ${user.last_name}"
    override val photoUrl: String = user.photo_max
    override val gender: String
        get() {
            val fields = user.fields
            if (!fields.has("sex"))
                return ""

            return when(fields.getInt("sex")) {
                1 -> GENDER_FEMALE
                2 -> GENDER_MALE
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