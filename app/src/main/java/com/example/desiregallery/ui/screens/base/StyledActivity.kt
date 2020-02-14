package com.example.desiregallery.ui.screens.base

import android.os.Bundle
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import javax.inject.Inject

/**
 * @author babaetskv on 16.12.19
 */
abstract class StyledActivity(private val fullScreen: Boolean = false) : BaseActivity() {
    @Inject
    lateinit var prefsHelper: IDGSharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.appComponent.inject(this)
        val themeRes = if (fullScreen) {
            if (prefsHelper.darkModeOn) R.style.AppTheme_SplashScreen_Dark else R.style.AppTheme_SplashScreen
        } else {
            if (prefsHelper.darkModeOn) R.style.AppTheme_Dark else R.style.AppTheme
        }
        setTheme(themeRes)
    }
}
