package com.example.desiregallery.ui.activities

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

/**
 * @author babaetskv
 * */
class SignUpActivity : AppCompatActivity() {
    private val TAG = SignUpActivity::class.java.simpleName

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

            if (CheckLoginTask().execute(login).get()) {
                val newUser = User(login, password)
                newUser.setEmail(email)
                newUser.setGender(gender)
                newUser.setBirthday(birthday)
                registerUser(newUser)
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
                Log.e(TAG, "Unable to sign up")
                t.printStackTrace()
            }
        })
    }

    internal inner class CheckLoginTask : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg params: String?): Boolean {
            val response: Response<List<User>> = DGNetwork.getService().getUsers().execute()
            if (response.isSuccessful) {
                val users = response.body()
                if (users != null) {
                    val logins = users.map { it.getLogin() }
                    if (params[0] in logins) {
                        Toast.makeText(applicationContext, R.string.non_unique_login, Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
            }
            else {
                Toast.makeText(applicationContext, R.string.sign_up_error, Toast.LENGTH_LONG).show()
                Log.e(TAG, "Unable to sign up")
                return false
            }
            return true
        }
    }
}
