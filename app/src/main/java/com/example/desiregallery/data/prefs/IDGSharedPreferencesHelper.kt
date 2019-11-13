package com.example.desiregallery.data.prefs

import com.example.desiregallery.auth.AuthMethod

/**
 * @author babaetskv on 17.09.19
 */
interface IDGSharedPreferencesHelper {
    fun setAuthMethod(method: AuthMethod)
    fun getAuthMethod(): AuthMethod?
    fun clearAuthMethod()
    fun hasAuthMethod(): Boolean
}
