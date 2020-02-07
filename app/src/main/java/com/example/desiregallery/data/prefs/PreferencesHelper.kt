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

    override val hasAuthMethod: Boolean
        get() = appPrefs.contains(PREF_AUTH_METHOD)
    override var authMethod: AuthMethod?
        get() {
            val methodName = lockDecorator {
                appPrefs.getString(PREF_AUTH_METHOD, null)
            }
            return if (methodName == null) methodName else AuthMethod.fromString(methodName)
        }
        set(value) {
            lockDecorator {
                appPrefs.edit().putString(PREF_AUTH_METHOD, value?.name).apply()
            }
        }
    override var darkModeOn: Boolean
        get() = lockDecorator {
            appPrefs.getBoolean(PREF_DARK_MODE, false)
        }
        set(value) {
            lockDecorator {
                appPrefs.edit().putBoolean(PREF_DARK_MODE, value).apply()
            }
        }
    override var startWithSettings: Boolean
        get() {
            return lockDecorator {
                appPrefs.getBoolean(PREF_START_WITH_SETTINGS, false)
            }
        }
        set(value) {
            lockDecorator {
                appPrefs.edit().putBoolean(PREF_START_WITH_SETTINGS, value).apply()
            }
        }

    override fun clearAuthMethod() {
        lockDecorator {
            appPrefs.edit().remove(PREF_AUTH_METHOD).apply()
        }
    }

    private inline fun <T : Any?> lockDecorator(f: () -> T): T {
        mPrefsLock.lock()
        val result = f()
        mPrefsLock.unlock()
        return result
    }

    companion object {
        private const val APP_PREFERENCES = "app_prefs"
        private const val PREF_AUTH_METHOD = "auth_method"
        private const val PREF_DARK_MODE = "dark_mode"
        private const val PREF_START_WITH_SETTINGS = "start_with_settings"
    }
}
