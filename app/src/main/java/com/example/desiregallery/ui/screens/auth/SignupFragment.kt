package com.example.desiregallery.ui.screens.auth

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.transition.Slide
import com.example.desiregallery.R
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.auth.AuthMethod
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.BaseNetworkService
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.toolbar_signup.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * @author babaetskv on 11.11.19
 */
class SignupFragment private constructor(manager: IAuthFragmentManager): BaseAuthFragment(manager) {
    private val analytics: IDGAnalyticsTracker by inject()
    private val auth: FirebaseAuth by inject()

    private lateinit var inputTextWatcher: TextWatcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signup_button_back.setOnClickListener {
            manager.setLoginFragment(Slide(Gravity.START), Slide(Gravity.START))
        }
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
        sign_up_input_birthday.addTextChangedListener(inputTextWatcher)
        sign_up_input_password.addTextChangedListener(inputTextWatcher)
        sign_up_input_confirm.addTextChangedListener(inputTextWatcher)

        sign_up_input_birthday.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val date = getString(R.string.date_format, dayOfMonth, month+1, year)
                sign_up_input_birthday.setText(date)
            }
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireActivity(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show()
        }
        sign_up_button.setOnClickListener {
            disableAll()
            val login = sign_up_input_login.text.toString()
            val email = sign_up_input_email.text.toString()
            val birthday = sign_up_input_birthday.text.toString()
            val password = sign_up_input_password.text.toString()
            val passwordConfirm = sign_up_input_confirm.text.toString()

            if (password != passwordConfirm) {
                Toast.makeText(requireContext(), R.string.non_equal_passwords, Toast.LENGTH_SHORT)
                    .show()
                enableAll()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    logInfo(TAG, "VKUser $login successfully signed up")
                    saveUserInfo(User(email, password).also {
                        it.login = login
                        it.birthday = birthday
                    })
                    enableAll()
                    analytics.trackSignUp(AuthMethod.EMAIL)
                    manager.setLoginFragment(Slide(Gravity.START), Slide(Gravity.START))
                } else {
                    logError(TAG, "Failed to sign up: ${task.exception?.message}")
                    Toast.makeText(requireContext(),
                        getString(R.string.sign_up_error, task.exception?.message),
                        Toast.LENGTH_LONG).show()
                    enableAll()
                }

            }
        }
    }

    private fun saveUserInfo(user: User) {
        val baseService: BaseNetworkService = get()
        baseService.createUser(user.login, user).enqueue(object: Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {
                logInfo(TAG, "Data of user ${user.login} have successfully been saved to firestore")
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                logError(TAG, "Unable to save user data to firestore: ${t.message}")
            }
        })

        val firebaseUser = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(user.login).build()
        firebaseUser?.updateProfile(profileUpdates)?.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful)
                logInfo(TAG, "Data of user ${user.login} have successfully been saved to firebase auth")
            else
                logError(TAG, "Unable to save user data to firebase auth: ${task.exception?.message}")
        }
    }

    private fun checkForEmptyFields() {
        sign_up_button.isEnabled = arrayOf(
            sign_up_input_confirm.text.toString(),
            sign_up_input_password.text.toString(),
            sign_up_input_birthday.text.toString(),
            sign_up_input_email.text.toString(),
            sign_up_input_login.text.toString()
        ).all { fieldIsValid(it) }
    }

    private fun enableAll() {
        sign_up_input_confirm.isEnabled = true
        sign_up_input_password.isEnabled = true
        sign_up_input_birthday.isEnabled = true
        sign_up_input_email.isEnabled = true
        sign_up_input_login.isEnabled = true
        sign_up_button.isEnabled = true
    }

    private fun disableAll() {
        sign_up_input_confirm.isEnabled = false
        sign_up_input_password.isEnabled = false
        sign_up_input_birthday.isEnabled = false
        sign_up_input_email.isEnabled = false
        sign_up_input_login.isEnabled = false
        sign_up_button.isEnabled = false
    }

    private fun fieldIsValid(field: String) = Regex("\\S+").matches(field)

    companion object {
        private val TAG = SignupFragment::class.java.simpleName

        fun createInstance(manager: IAuthFragmentManager) =
            SignupFragment(manager)
    }
}