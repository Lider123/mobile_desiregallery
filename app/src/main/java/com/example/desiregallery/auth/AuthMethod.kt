package com.example.desiregallery.auth

/**
 * @author babaetskv on 17.09.19
 */
enum class AuthMethod(val methodName: String) {
    EMAIL("email"),
    VK("vk"),
    GOOGLE("google");

    companion object {
        private val map = values().associateBy(AuthMethod::methodName)

        fun fromString(method: String) = map[method]
    }
}