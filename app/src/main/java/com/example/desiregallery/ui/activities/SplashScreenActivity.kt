package com.example.desiregallery.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.desiregallery.R

class SplashScreenActivity : AppCompatActivity() {

    private val TIMEOUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val r = Runnable {
            val i = Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
        Handler().postDelayed(r, TIMEOUT)
    }
}
