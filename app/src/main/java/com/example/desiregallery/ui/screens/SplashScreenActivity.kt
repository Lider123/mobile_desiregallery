package com.example.desiregallery.ui.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.ui.screens.auth.LoginActivity
import com.example.desiregallery.ui.screens.base.StyledActivity
import javax.inject.Inject

class SplashScreenActivity : StyledActivity(true) {
    @Inject
    lateinit var prefs: IDGSharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        MainApplication.appComponent.inject(this)

        if (prefs.hasAuthMethod) goToMainActivity() else {
            Handler().postDelayed(this::goToLoginActivity, TIMEOUT)
        }
    }

    private fun goToLoginActivity() {
        Intent(applicationContext, LoginActivity::class.java).also {
            startActivity(it)
        }
        finish()
    }

    private fun goToMainActivity() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
        finish()
    }

    companion object {
        private var TIMEOUT = 2500L
    }
}
