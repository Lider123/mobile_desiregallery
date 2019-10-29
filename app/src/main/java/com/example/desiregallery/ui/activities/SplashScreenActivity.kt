package com.example.desiregallery.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.desiregallery.R
import com.example.desiregallery.sharedprefs.IDGSharedPreferencesHelper
import org.koin.android.ext.android.inject

class SplashScreenActivity : AppCompatActivity() {
    private val prefs: IDGSharedPreferencesHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (prefs.hasAuthMethod())
            goToMainActivity()
        else {
            val r = Runnable(this::goToLoginActivity)
            Handler().postDelayed(r, TIMEOUT)
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private var TIMEOUT = 2500L
    }
}
