package com.example.desiregallery.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.desiregallery.models.User
import com.example.desiregallery.network.DGNetwork
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.AsyncTask
import com.example.desiregallery.R
import android.text.Editable
import android.text.TextWatcher
import java.lang.ref.WeakReference


class SignUpActivity : AppCompatActivity() {
    private lateinit var inputTextWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        inputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                checkForEmptyFields()
            }
        }
        sign_up_button.isEnabled = false
        initListeners()
    }

    private fun initListeners() {
        sign_up_input_login.addTextChangedListener(inputTextWatcher)
        sign_up_input_email.addTextChangedListener(inputTextWatcher)
        sign_up_input_gender.addTextChangedListener(inputTextWatcher)
        sign_up_input_birthday.addTextChangedListener(inputTextWatcher)
        sign_up_input_password.addTextChangedListener(inputTextWatcher)
        sign_up_input_confirm.addTextChangedListener(inputTextWatcher)
        sign_up_button.setOnClickListener {
            val login = sign_up_input_login.text.toString()
            val email = sign_up_input_email.text.toString()
            val gender = sign_up_input_gender.text.toString()
            val birthday = sign_up_input_birthday.text.toString()
            val password = sign_up_input_password.text.toString()
            val passwordConfirm = sign_up_input_confirm.text.toString()

            if (password != passwordConfirm) {
                Toast.makeText(applicationContext, R.string.non_equal_passwords, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (CheckLoginTask(applicationContext).execute(login).get()) {
                registerUser(User(login, password).also {
                    it.email = email
                    it.gender = gender
                    it.birthday = birthday
                })
            }
        }
    }

    private fun checkForEmptyFields() {
        sign_up_button.isEnabled = arrayOf(
            sign_up_input_confirm.text.toString(),
            sign_up_input_password.text.toString(),
            sign_up_input_birthday.text.toString(),
            sign_up_input_gender.text.toString(),
            sign_up_input_email.text.toString(),
            sign_up_input_login.text.toString()
        ).all { fieldIsValid(it) }
    }

    private fun fieldIsValid(field: String) = Regex("\\S+").matches(field)

    private fun registerUser(user: User) {
        DGNetwork.getService().createUser(user.getLogin(), user).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.i(TAG, String.format("User %s have successfully been signed up", user.getLogin()))
                onBackPressed()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, R.string.sign_up_error, Toast.LENGTH_LONG).show()
                Log.e(TAG, "Unable to sign up: ${t.message}")
            }
        })
    }

    companion object {
        private val TAG = SignUpActivity::class.java.simpleName

        internal class CheckLoginTask(context: Context) : AsyncTask<String, Void, Boolean>() {
            private val contextRef = WeakReference<Context>(context)

            override fun doInBackground(vararg params: String?): Boolean {
                val response = DGNetwork.getService().getUsers().execute()
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let {
                        val logins = it.map { user -> user.getLogin() }
                        if (params[0] in logins) {
                            Toast.makeText(contextRef.get(), R.string.non_unique_login, Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
                }
                else {
                    Toast.makeText(contextRef.get(), R.string.sign_up_error, Toast.LENGTH_LONG).show()
                    Log.e(TAG, "Unable to sign up")
                    return false
                }
                return true
            }
        }
    }
}
