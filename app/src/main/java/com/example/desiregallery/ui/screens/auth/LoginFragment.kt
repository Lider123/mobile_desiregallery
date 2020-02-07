package com.example.desiregallery.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.analytics.IDGAnalyticsTracker
import com.example.desiregallery.auth.AuthMethod
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author babaetskv on 18.11.19
 */
class LoginFragment : Fragment() {
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var googleClient: GoogleSignInClient
    @Inject
    lateinit var analytics: IDGAnalyticsTracker
    @Inject
    lateinit var prefsHelper: IDGSharedPreferencesHelper

    private lateinit var snackbar: SnackbarWrapper
    private lateinit var mDisposable: Disposable

    lateinit var mLoginListener: ILoginListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MainApplication.appComponent.inject(this)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackbar = SnackbarWrapper(login_container)
        button_sign_in.isEnabled = false
        mDisposable = createTextChangedObservable().toFlowable(BackpressureStrategy.LATEST)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { checkForEmptyFields() }
        initListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (requireActivity() is ILoginListener) {
            mLoginListener = requireActivity() as ILoginListener
        } else throw Exception("Parent activity doesn't implement login interface")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(
                requestCode,
                resultCode,
                data,
                object : VKCallback<VKAccessToken> {

                    override fun onResult(res: VKAccessToken?) {
                        Timber.i("Successfully logged in with vk")
                        prefsHelper.authMethod = AuthMethod.VK
                        analytics.trackLogin(AuthMethod.VK)
                        mLoginListener.onSuccessfulLogin()
                    }

                    override fun onError(error: VKError?) {
                        if (error?.errorCode == VKError.VK_CANCELED) return
                        Timber.e("Failed to sign in with vk: ${error?.errorMessage}")
                        snackbar.show(getString(R.string.sign_in_vk_failure))
                    }
                })
        ) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleGoogleSignInResult(task)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (!mDisposable.isDisposed) mDisposable.dispose()
    }

    private fun createTextChangedObservable(): Observable<String> {
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
            input_email.addTextChangedListener(inputTextWatcher)
            input_password.addTextChangedListener(inputTextWatcher)
            emitter.setCancellable {
                input_email.removeTextChangedListener(inputTextWatcher)
                input_password.removeTextChangedListener(inputTextWatcher)
            }
        }
        return observable.debounce(1, TimeUnit.SECONDS)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Timber.i("Successfully signed in google account ${account?.displayName}")
            prefsHelper.authMethod = AuthMethod.GOOGLE
            analytics.trackLogin(AuthMethod.GOOGLE)
            mLoginListener.onSuccessfulLogin()
        } catch (e: ApiException) {
            if (e.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) return
            Timber.e(e, "Failed to sign in with google")
            snackbar.show(getString(R.string.sign_in_google_failure))
        }
    }

    private fun initListeners() {
        button_sign_in.setOnClickListener {
            val email = input_email.text.toString()
            val password = input_password.text.toString()
            logIn(email, password)
        }
        button_sign_in_vk.setOnClickListener {
            VKSdk.login(requireActivity(), VKScope.FRIENDS, VKScope.OFFLINE)
        }
        button_sign_in_google.setOnClickListener {
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
        }
        link_sign_up.setOnClickListener {
            val intent = Intent(requireActivity(), SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logIn(email: String, password: String) {
        showProgress()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                hideProgress()
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Timber.i("User with email ${user?.email} successfully logged in")
                    prefsHelper.authMethod = AuthMethod.EMAIL
                    analytics.trackLogin(AuthMethod.EMAIL)
                    mLoginListener.onSuccessfulLogin()
                } else {
                    Timber.e(task.exception, "Failed to login with email")
                    snackbar.show(getString(R.string.login_error, task.exception?.localizedMessage))
                }
            }
    }

    private fun checkForEmptyFields() {
        button_sign_in.isEnabled = listOf<TextView>(input_email, input_password).all { textView ->
            textView.text.toString().trim().isNotEmpty()
        }
    }

    private fun showProgress() {
        login_progress.visibility = View.VISIBLE
        arrayOf(
            input_email,
            input_password,
            link_sign_up,
            button_sign_in,
            button_sign_in_vk,
            button_sign_in_google
        ).forEach {
            it.isEnabled = false
        }
    }

    private fun hideProgress() {
        login_progress.visibility = View.GONE
        arrayOf(
            input_email,
            input_password,
            link_sign_up,
            button_sign_in,
            button_sign_in_vk,
            button_sign_in_google
        ).forEach {
            it.isEnabled = true
        }
    }

    companion object {
        private const val GOOGLE_SIGN_IN_REQUEST_CODE = 1
    }
}
