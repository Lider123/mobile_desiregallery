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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError

class LoginActivity : AppCompatActivity() {
    companion object {
        private const val GOOGLE_SIGN_IN_REQUEST_CODE = 1
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
        button_sign_in.isEnabled = false
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
                    if (error?.errorCode == VKError.VK_CANCELED)
                        return
                    Log.e(TAG, "Failed to sign in with vk")
                    Toast.makeText(this@LoginActivity, R.string.sign_in_vk_failure, Toast.LENGTH_SHORT).show()
                }
            })) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode  == GOOGLE_SIGN_IN_REQUEST_CODE) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            }
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.i(TAG, "Successfully signed in google account ${account?.displayName}")
            PreferencesHelper(this).setAuthMethod(AuthMethod.GOOGLE)
            goToMainActivity()
        }
        catch (e: ApiException) {
            if (e.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED)
                return
            Log.e(TAG, "Failed to sign in with google: ${e.message}")
            Toast.makeText(this@LoginActivity, R.string.sign_in_google_failure, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListeners() {
        input_email.addTextChangedListener(inputTextWatcher)
        input_password.addTextChangedListener(inputTextWatcher)
        button_sign_in.setOnClickListener {
            val email = input_email.text.toString()
            val password = input_password.text.toString()
            logIn(email, password)
        }
        button_sign_in_vk.setOnClickListener { VKSdk.login(this, VKScope.FRIENDS, VKScope.OFFLINE) }
        button_sign_in_google.setOnClickListener {
            val client = MainApplication.instance.googleSignInClient
            startActivityForResult(client.signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
        }
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
        button_sign_in.isEnabled = input_email.text.toString().trim().isNotEmpty() && input_password.text.toString().trim().isNotEmpty()
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
        button_sign_in.isEnabled = false
        button_sign_in_vk.isEnabled = false
        button_sign_in_google.isEnabled = false
    }

    private fun hideProgress() {
        login_progress.visibility = View.GONE
        input_email.isEnabled = true
        input_password.isEnabled = true
        link_sign_up.isEnabled = true
        button_sign_in.isEnabled = true
        button_sign_in_vk.isEnabled = true
        button_sign_in_google.isEnabled = true
    }
}
