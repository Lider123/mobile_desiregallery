package com.example.desiregallery.data.prefs

import android.content.Context
import com.example.desiregallery.auth.AuthMethod
import java.util.concurrent.locks.ReentrantLock

/**
 * @author babaetskv on 17.09.19
 */
class PreferencesHelper(context: Context) : IDGSharedPreferencesHelper {
    private val appPrefs = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    private val mPrefsLock = ReentrantLock()

    override fun setAuthMethod(method: AuthMethod) {
        mPrefsLock.lock()
        appPrefs.edit().putString(PREF_AUTH_METHOD, method.name).apply()
        mPrefsLock.unlock()
    }

    override fun getAuthMethod(): AuthMethod? {
        mPrefsLock.lock()
        val methodName = appPrefs.getString(PREF_AUTH_METHOD, null)
        mPrefsLock.unlock()
        return if (methodName == null)
            methodName
        else
            AuthMethod.fromString(methodName)
    }

    override fun clearAuthMethod() {
        mPrefsLock.lock()
        appPrefs.edit().remove(PREF_AUTH_METHOD).apply()
        mPrefsLock.unlock()
    }

    override fun hasAuthMethod() = appPrefs.contains(PREF_AUTH_METHOD)

    companion object {
        private const val APP_PREFERENCES = "app_prefs"
        private const val PREF_AUTH_METHOD = "auth_method"
    }
}
