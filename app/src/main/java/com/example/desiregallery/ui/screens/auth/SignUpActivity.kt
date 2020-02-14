package com.example.desiregallery.ui.screens.auth

import android.app.DatePickerDialog
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_up.*
import android.text.Editable
import android.text.TextWatcher
import com.example.desiregallery.MainApplication
import android.view.View
import com.example.desiregallery.R
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.auth.AuthMethod
import com.example.desiregallery.data.Result
import com.example.desiregallery.data.models.User
import com.example.desiregallery.data.network.NetworkManager
import com.example.desiregallery.ui.screens.base.StyledActivity
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.toolbar_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignUpActivity : StyledActivity(), View.OnClickListener {
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var analytics: IDGAnalyticsTracker
    @Inject
    lateinit var networkManager: NetworkManager

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
    private lateinit var snackbar: SnackbarWrapper
    private lateinit var mDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        MainApplication.appComponent.inject(this)
        snackbar = SnackbarWrapper(sign_up_container)
        sign_up_button_back.setOnClickListener {
            onBackPressed()
        }

        sign_up_button.isEnabled = false
        sign_up_input_birthday.setOnClickListener(this)
        sign_up_button.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        mDisposable = createTextChangeObservable().toFlowable(BackpressureStrategy.LATEST)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                checkForEmptyFields()
                hideError()
            }
    }

    override fun onStop() {
        super.onStop()
        if (!mDisposable.isDisposed) mDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        parentJob.cancel()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_up_input_birthday -> {
                hideError()
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val date = getString(R.string.date_format, dayOfMonth, month + 1, year)
                        sign_up_input_birthday.setText(date)
                    }
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    this,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.sign_up_button -> {
                disableAll()
                hideError()
                signUp()
            }
        }
    }

    private fun signUp() {
        val login = sign_up_input_login.text.toString()
        val email = sign_up_input_email.text.toString()
        val birthday = sign_up_input_birthday.text.toString()
        val password = sign_up_input_password.text.toString()
        val passwordConfirm = sign_up_input_confirm.text.toString()

        if (password != passwordConfirm) {
            showError(getText(R.string.non_equal_passwords).toString())
            enableAll()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Timber.i("VKUser $login successfully signed up")
                    saveUserInfo(User(email, password).also {
                        it.login = login
                        it.birthday = birthday
                    })
                    enableAll()
                    analytics.trackSignUp(AuthMethod.EMAIL)
                    onBackPressed()
                } else {
                    Timber.e(task.exception, "Failed to sign up")
                    val message = task.exception?.localizedMessage
                        ?: getString(R.string.sign_up_error, getString(R.string.unknown_error))
                    snackbar.show(message)
                    enableAll()
                }
            }
    }

    private fun createTextChangeObservable(): Observable<String> {
        val observable = Observable.create<String> { emitter ->
            val inputTextWatcher = object : TextWatcher {

                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i2: Int,
                    i3: Int
                ) = Unit

                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) =
                    Unit

                override fun afterTextChanged(editable: Editable) {
                    emitter.onNext(editable.toString())
                }
            }
            val inputFields = listOf(
                sign_up_input_login,
                sign_up_input_email,
                sign_up_input_birthday,
                sign_up_input_password,
                sign_up_input_confirm
            )
            inputFields.forEach { it.addTextChangedListener(inputTextWatcher) }
            emitter.setCancellable {
                inputFields.forEach { it.removeTextChangedListener(inputTextWatcher) }
            }
        }
        return observable.debounce(1, TimeUnit.SECONDS)
    }

    private fun saveUserInfo(user: User) {
        coroutineScope.launch(Dispatchers.Main) {
            when (val result = networkManager.createUser(user)) {
                is Result.Success -> Timber.i("User ${user.login} has been successfully created")
                is Result.Error -> Timber.e(result.exception, "Failed to create user ${user.login}")
            }
        }

        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(user.login).build()
        auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Timber.i("Data of user ${user.login} have successfully been saved to firebase auth")
            } else Timber.e(task.exception, "Unable to save user data to firebase auth")
        }
    }

    private fun checkForEmptyFields() {
        sign_up_button.isEnabled = arrayOf(
            sign_up_input_confirm,
            sign_up_input_password,
            sign_up_input_birthday,
            sign_up_input_email,
            sign_up_input_login
        ).map { it.text.toString() }.all { fieldIsValid(it) }
    }

    private fun enableAll() {
        listOf(
            sign_up_input_confirm,
            sign_up_input_password,
            sign_up_input_birthday,
            sign_up_input_email,
            sign_up_input_login,
            sign_up_button
        ).forEach { it.isEnabled = true }
    }

    private fun disableAll() {
        listOf(
            sign_up_input_confirm,
            sign_up_input_password,
            sign_up_input_birthday,
            sign_up_input_email,
            sign_up_input_login,
            sign_up_button
        ).forEach { it.isEnabled = false }
    }

    private fun fieldIsValid(field: String) = Regex("\\S+").matches(field)

    private fun hideError() {
        error_message.visibility = View.GONE
    }

    private fun showError(message: String) {
        error_message.text = message
        error_message.visibility = View.VISIBLE
    }
}
