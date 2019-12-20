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

    override val hasAuthMethod: Boolean get() = appPrefs.contains(PREF_AUTH_METHOD)
    override var authMethod: AuthMethod?
        get() {
            mPrefsLock.lock()
            val methodName = appPrefs.getString(PREF_AUTH_METHOD, null)
            mPrefsLock.unlock()
            return if (methodName == null) methodName else AuthMethod.fromString(methodName)
        }
        set(value) {
            mPrefsLock.lock()
            appPrefs.edit().putString(PREF_AUTH_METHOD, value?.name).apply()
            mPrefsLock.unlock()
        }
    override var darkModeOn: Boolean
        get() {
            mPrefsLock.lock()
            val darkModeOn = appPrefs.getBoolean(PREF_DARK_MODE, false)
            mPrefsLock.unlock()
            return darkModeOn
        }
        set(value) {
            mPrefsLock.lock()
            appPrefs.edit().putBoolean(PREF_DARK_MODE, value).apply()
            mPrefsLock.unlock()
        }
    override var startWithSettings: Boolean
        get() {
            mPrefsLock.lock()
            val startWithSettings = appPrefs.getBoolean(PREF_START_WITH_SETTINGS, false)
            mPrefsLock.unlock()
            return startWithSettings
        }
        set(value) {
            mPrefsLock.lock()
            appPrefs.edit().putBoolean(PREF_START_WITH_SETTINGS, value).apply()
            mPrefsLock.unlock()
        }

    override fun clearAuthMethod() {
        mPrefsLock.lock()
        appPrefs.edit().remove(PREF_AUTH_METHOD).apply()
        mPrefsLock.unlock()
    }

    companion object {
        private const val APP_PREFERENCES = "app_prefs"
        private const val PREF_AUTH_METHOD = "auth_method"
        private const val PREF_DARK_MODE = "dark_mode"
        private const val PREF_START_WITH_SETTINGS = "start_with_settings"
    }
}
