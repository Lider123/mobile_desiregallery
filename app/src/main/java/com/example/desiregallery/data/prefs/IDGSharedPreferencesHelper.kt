package com.example.desiregallery.data.prefs

import com.example.desiregallery.auth.AuthMethod

/**
 * @author babaetskv on 17.09.19
 */
interface IDGSharedPreferencesHelper {
    val hasAuthMethod: Boolean
    var authMethod: AuthMethod?
    fun clearAuthMethod()
}
