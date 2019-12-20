package com.example.desiregallery.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import com.example.desiregallery.R
import com.example.desiregallery.ui.screens.MainActivity
import com.example.desiregallery.ui.screens.base.BaseActivity

class LoginActivity : BaseActivity(true), ILoginListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onSuccessfulLogin() = goToMainActivity()

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
