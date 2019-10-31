package com.example.desiregallery.auth

/**
 * @author babaetskv on 17.09.19
 */
enum class AuthMethod {
    EMAIL,
    VK,
    GOOGLE;

    companion object {
        private val map = values().associateBy(AuthMethod::name)

        fun fromString(method: String) = map[method]
    }
}
