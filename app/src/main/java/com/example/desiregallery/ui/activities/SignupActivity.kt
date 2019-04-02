package com.example.desiregallery.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.desiregallery.models.User
import com.example.desiregallery.network.DGNetwork
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.AsyncTask
import com.example.desiregallery.R
import android.text.Editable
import android.text.TextWatcher




class SignupActivity : AppCompatActivity() {

    private val TAG = SignupActivity::class.java.simpleName

    private lateinit var inputTextWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        inputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                checkForEmptyFields()
            }
        }
        signup_button.isEnabled = false
        initListeners()
    }

    private fun initListeners() {
        signup_input_login.addTextChangedListener(inputTextWatcher)
        signup_input_password.addTextChangedListener(inputTextWatcher)
        signup_input_confirm.addTextChangedListener(inputTextWatcher)
        signup_button.setOnClickListener {
            val login = signup_input_login.text.toString()
            val password = signup_input_password.text.toString()
            val passwordConfirm = signup_input_confirm.text.toString()

            if (password != passwordConfirm) {
                Toast.makeText(applicationContext, R.string.non_equal_passwords, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (CheckLoginTask().execute(login).get()) {
                val newUser = User()
                newUser.setLogin(login)
                newUser.setPassword(password)
                registerUser(newUser)
            }
        }
    }

    private fun checkForEmptyFields() {
        signup_button.isEnabled = fieldIsValid(signup_input_confirm.text.toString())
                && fieldIsValid(signup_input_password.text.toString())
                && fieldIsValid(signup_input_login.text.toString())
    }

    private fun fieldIsValid(field: String): Boolean {
        if (field.isEmpty())
            return false
        if (field != field.replace(" ", ""))
            return false

        return true
    }

    private fun registerUser(user: User) {
        DGNetwork.getService().createUser(user.getLogin(), user).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.i(TAG, String.format("User %s have successfully been signed up", user.getLogin()))
                onBackPressed()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, R.string.signup_error, Toast.LENGTH_LONG).show()
                Log.e(TAG, "Unable to sign up")
                t.printStackTrace()
            }
        })
    }

    internal inner class CheckLoginTask : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg params: String?): Boolean {
            val response: Response<List<User>> = DGNetwork.getService().getUsers().execute()
            if (response.isSuccessful && response.body() != null) {
                val users = response.body()
                val logins = ArrayList<String>()
                for (user in users!!.iterator()) {
                    logins.add(user.getLogin())
                }
                if (params[0] in logins) {
                    Toast.makeText(applicationContext, R.string.non_unique_login, Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            else {
                Toast.makeText(applicationContext, R.string.signup_error, Toast.LENGTH_LONG).show()
                Log.e(TAG, "Unable to sign up")
                return false
            }
            return true
        }
    }
}
