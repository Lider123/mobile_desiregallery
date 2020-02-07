package com.example.desiregallery.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import com.example.desiregallery.R
import com.example.desiregallery.ui.screens.MainActivity
import com.example.desiregallery.ui.screens.base.StyledActivity

class LoginActivity : StyledActivity(true), ILoginListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onSuccessfulLogin() = goToMainActivity()

    private fun goToMainActivity() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
        finish()
    }
}
