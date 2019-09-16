package com.example.desiregallery.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.desiregallery.R
import kotlinx.android.synthetic.main.activity_login.*
import com.example.desiregallery.MainApplication
import com.example.desiregallery.auth.AuthMethod
import com.example.desiregallery.sharedprefs.PreferencesHelper
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError


class LoginActivity : AppCompatActivity() {
    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

    private lateinit var inputTextWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                checkForEmptyFields()
            }
        }
        button_login.isEnabled = false
        initListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object: VKCallback<VKAccessToken> {

                override fun onResult(res: VKAccessToken?) {
                    Log.i(TAG, "Successfully logged in with vk")
                    PreferencesHelper(this@LoginActivity).setAuthMethod(AuthMethod.VK)
                    goToMainActivity()
                }

                override fun onError(error: VKError?) {
                    Log.e(TAG, "Failed to log in with vk")
                }
            })) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initListeners() {
        input_email.addTextChangedListener(inputTextWatcher)
        input_password.addTextChangedListener(inputTextWatcher)
        button_login.setOnClickListener {
            val email = input_email.text.toString()
            val password = input_password.text.toString()
            logIn(email, password)
        }
        button_login_vk.setOnClickListener { VKSdk.login(this, VKScope.FRIENDS, VKScope.OFFLINE) }
        link_sign_up.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logIn(email: String, password: String) {
        showProgress()
        MainApplication.getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            hideProgress()
            if (task.isSuccessful) {
                val user = MainApplication.getAuth().currentUser
                Log.i(TAG, "VKUser with email ${user?.email} successfully logged in")
                PreferencesHelper(this).setAuthMethod(AuthMethod.EMAIL)
                goToMainActivity()
            } else {
                Log.w(TAG, "Failed to login: ", task.exception)
                Toast.makeText(baseContext, getString(R.string.login_error, task.exception?.message), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkForEmptyFields() {
        button_login.isEnabled = input_email.text.toString().trim().isNotEmpty() && input_password.text.toString().trim().isNotEmpty()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showProgress() {
        login_progress.visibility = View.VISIBLE
        input_email.isEnabled = false
        input_password.isEnabled = false
        link_sign_up.isEnabled = false
        button_login.isEnabled = false
    }

    private fun hideProgress() {
        login_progress.visibility = View.GONE
        input_email.isEnabled = true
        input_password.isEnabled = true
        link_sign_up.isEnabled = true
        button_login.isEnabled = true
    }
}
