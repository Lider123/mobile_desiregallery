package com.example.desiregallery.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.desiregallery.R
import com.example.desiregallery.ui.screens.MainActivity

class LoginActivity : FragmentActivity(), ILoginListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSuccessfulLogin() = goToMainActivity()
}
