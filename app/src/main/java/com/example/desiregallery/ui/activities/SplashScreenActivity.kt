package com.example.desiregallery.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R

class SplashScreenActivity : AppCompatActivity() {
    companion object {
        private var TIMEOUT = 3000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val prefs = getSharedPreferences(MainApplication.APP_PREFERENCES, Context.MODE_PRIVATE)
        if (prefs.contains(MainApplication.PREFS_CURR_USER_KEY))
            TIMEOUT = 1000L

        val r = Runnable {
            val i = Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
        Handler().postDelayed(r, TIMEOUT)
    }
}
