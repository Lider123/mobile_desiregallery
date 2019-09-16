package com.example.desiregallery.auth

import com.vk.sdk.api.model.VKApiUser

/**
 * @author babaetskv on 17.09.19
 */

class VKAccount(val user: VKApiUser) : IAccount {

    override fun getAccessToken(): String  {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDisplayName() = "${user.first_name} ${user.last_name}"

    override fun getPhotoUrl(): String = user.photo_max

    override fun getGender(): String {
        val fields = user.fields
        if (!fields.has("sex"))
            return ""

        return when(fields.getInt("sex")) {
            1 -> "female"
            2 -> "male"
            else -> ""
        }
    }

    override fun getBirthday(): String {
        val fields = user.fields
        if (!fields.has("bdate"))
            return ""

        return fields.getString("bdate")
    }
}