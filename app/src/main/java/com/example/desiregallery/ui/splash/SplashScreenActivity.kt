package com.example.desiregallery.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.desiregallery.R
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.ui.login.LoginActivity
import com.example.desiregallery.ui.main.MainActivity
import org.koin.android.ext.android.get

class SplashScreenActivity : AppCompatActivity() {
    private val prefs: IDGSharedPreferencesHelper = get()

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
