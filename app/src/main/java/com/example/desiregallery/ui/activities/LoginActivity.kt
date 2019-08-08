package com.example.desiregallery.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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


class LoginActivity : AppCompatActivity() {
    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

    private lateinit var prefs: SharedPreferences
    private lateinit var inputTextWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences(MainApplication.APP_PREFERENCES, Context.MODE_PRIVATE)
        // TODO: check saved token and go to MainActivity

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

    private fun initListeners() {
        input_email.addTextChangedListener(inputTextWatcher)
        input_password.addTextChangedListener(inputTextWatcher)
        button_login.setOnClickListener {
            val email = input_email.text.toString()
            val password = input_password.text.toString()
            logIn(email, password)
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
                Log.i(TAG, "User with email ${user?.email} successfully logged in")
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
