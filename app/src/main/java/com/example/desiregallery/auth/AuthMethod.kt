package com.example.desiregallery.auth

/**
 * @author babaetskv on 17.09.19
 */
enum class AuthMethod(val value: String) {
    EMAIL("email"),
    VK("vk");

    companion object {
        private val map = values().associateBy(AuthMethod::value)

        fun fromString(method: String) = map[method]
    }
}