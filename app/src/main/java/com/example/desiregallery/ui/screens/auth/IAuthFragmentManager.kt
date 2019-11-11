package com.example.desiregallery.ui.screens.auth

import androidx.transition.Slide

/**
 * @author babaetskv on 11.11.19
 */
interface IAuthFragmentManager {
    fun setSplashScreenFragment(enter: Slide?, exit: Slide?)
    fun setLoginFragment(enter: Slide?, exit: Slide?)
    fun setSignupFragment(enter: Slide?, exit: Slide?)
}