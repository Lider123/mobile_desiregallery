package com.example.desiregallery.sharedprefs

import android.content.Context
import com.example.desiregallery.auth.AuthMethod

/**
 * @author babaetskv on 17.09.19
 */
class PreferencesHelper(context: Context) : IDGSharedPreferencesHelper {
    private val appPrefs = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    override fun setAuthMethod(method: AuthMethod) {
        appPrefs.edit().putString(PREF_AUTH_METHOD, method.value).apply()
    }

    override fun getAuthMethod(): AuthMethod? {
        val method = appPrefs.getString(PREF_AUTH_METHOD, null)
        return if (method == null)
            method
        else
            AuthMethod.fromString(method)
    }

    override fun clearAuthMethod() {
        appPrefs.edit().remove(PREF_AUTH_METHOD).apply()
    }

    override fun hasAuthMethod() = appPrefs.contains(PREF_AUTH_METHOD)

    companion object {
        private const val APP_PREFERENCES = "app_prefs"
        private const val PREF_AUTH_METHOD = "auth_method"
    }
}