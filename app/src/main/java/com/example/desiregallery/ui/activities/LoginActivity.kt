package com.example.desiregallery.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.desiregallery.R
import com.example.desiregallery.helpers.ModelGenerator
import kotlinx.android.synthetic.main.activity_login.*
import com.example.desiregallery.MainApplication
import com.example.desiregallery.models.User
import com.example.desiregallery.network.DGNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences(MainApplication.APP_PREFERENCES, Context.MODE_PRIVATE)
        val currUser = prefs.getString(MainApplication.PREFS_CURR_USER_KEY, null)
        if (currUser != null) {
            goToMainActivity()
        }

        button_login.setOnClickListener(View.OnClickListener {
            val login = input_login.text.toString()
            val password = input_password.text.toString()
            login(login, password)
        })
    }

    private fun login(login: String, password: String) {
        DGNetwork.getService().getUser(login).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body() == null) {
                    Toast.makeText(applicationContext, R.string.invalid_login, Toast.LENGTH_SHORT).show()
                    return
                }
                val user = response.body()
                if (user?.getPassword() != password) {
                    Toast.makeText(applicationContext, R.string.invalid_password, Toast.LENGTH_SHORT).show()
                    return
                }

                prefs.edit().putString(MainApplication.PREFS_CURR_USER_KEY, login).apply()
                Log.d(TAG, String.format("User %s logged in", login))
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
