package com.example.desiregallery.data.prefs

import android.content.Context
import com.example.desiregallery.auth.AuthMethod

/**
 * @author babaetskv on 17.09.19
 */
class PreferencesHelper(context: Context) : IDGSharedPreferencesHelper {
    private val appPrefs = context.getSharedPreferences(APP_PREFERENCES,
        Context.MODE_PRIVATE)

    override fun setAuthMethod(method: AuthMethod) {
        appPrefs.edit().putString(PREF_AUTH_METHOD, method.name).apply()
    }

    override fun getAuthMethod(): AuthMethod? {
        val methodName = appPrefs.getString(PREF_AUTH_METHOD, null)
        return if (methodName == null)
            methodName
        else
            AuthMethod.fromString(methodName)
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
