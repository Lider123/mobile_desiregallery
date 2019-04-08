package com.example.desiregallery.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.desiregallery.R
import kotlinx.android.synthetic.main.activity_login.*
import com.example.desiregallery.MainApplication
import com.example.desiregallery.database.DGDatabase
import com.example.desiregallery.models.User
import com.example.desiregallery.network.DGNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName

    private lateinit var prefs: SharedPreferences
    private lateinit var inputTextWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences(MainApplication.APP_PREFERENCES, Context.MODE_PRIVATE)
        val currUser = prefs.getString(MainApplication.PREFS_CURR_USER_KEY, null)
        if (currUser != null) {
            goToMainActivity()
        }

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
        input_login.addTextChangedListener(inputTextWatcher)
        input_password.addTextChangedListener(inputTextWatcher)
        button_login.setOnClickListener {
            val login = input_login.text.toString()
            val password = input_password.text.toString()
            login(login, password)
        }
        link_sign_up.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkForEmptyFields() {
        button_login.isEnabled = !(input_login.text.toString().trim().isEmpty() || input_password.text.toString().trim().isEmpty())
    }

    private fun login(login: String, password: String) {
        DGNetwork.getService().getUser(login).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                if (user == null) {
                    Toast.makeText(applicationContext, R.string.invalid_login, Toast.LENGTH_SHORT).show()
                    return
                }
                if (user.getPassword() != password) {
                    Toast.makeText(applicationContext, R.string.invalid_password, Toast.LENGTH_SHORT).show()
                    return
                }

                prefs.edit().putString(MainApplication.PREFS_CURR_USER_KEY, login).apply()
                Log.d(TAG, String.format("User %s logged in", login))
                DGDatabase.updateUser(user)
                goToMainActivity()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, R.string.login_error, Toast.LENGTH_LONG).show()
                Log.e(TAG, "Unable to log in")
            }
        })
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
