package com.example.desiregallery.auth

import com.vk.sdk.VKSdk
import com.vk.sdk.api.model.VKApiUser

/**
 * @author babaetskv on 17.09.19
 */
class VKAccount(private val user: VKApiUser) : IAccount {
    override val accessToken: String
        get() = "" // TODO
    override val displayName: String
        get() = "${user.first_name} ${user.last_name}"
    override val photoUrl: String
        get() = user.photo_max
    override val birthday: String
        get() {
            val fields = user.fields
            if (!fields.has("bdate")) return ""

            return fields.getString("bdate")
        }

    override fun logOut() = VKSdk.logout()
}
