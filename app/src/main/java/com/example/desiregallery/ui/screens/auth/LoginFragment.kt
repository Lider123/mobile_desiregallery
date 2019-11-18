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
import com.example.desiregallery.utils.logError
import com.example.desiregallery.utils.logInfo
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
import kotlinx.android.synthetic.main.fragment_login.*
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
    lateinit var prefs: IDGSharedPreferencesHelper

    private lateinit var snackbar: SnackbarWrapper

    private lateinit var inputTextWatcher: TextWatcher
    lateinit var mLoginListener: ILoginListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        MainApplication.appComponent.inject(this)
        inputTextWatcher = object: TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                checkForEmptyFields()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackbar = SnackbarWrapper(login_container)
        button_sign_in.isEnabled = false
        initListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (requireActivity() is ILoginListener)
            mLoginListener = requireActivity() as ILoginListener
        else
            throw Exception("Parent activity doesn't implement login interface")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object: VKCallback<VKAccessToken> {

                override fun onResult(res: VKAccessToken?) {
                    logInfo(TAG, "Successfully logged in with vk")
                    prefs.setAuthMethod(AuthMethod.VK)
                    analytics.trackLogin(AuthMethod.VK)
                    mLoginListener.onSuccessfulLogin()
                }

                override fun onError(error: VKError?) {
                    if (error?.errorCode == VKError.VK_CANCELED)
                        return
                    logError(
                        TAG,
                        "Failed to sign in with vk: ${error?.errorMessage}"
                    )
                    snackbar.show(getString(R.string.sign_in_vk_failure))
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
            logInfo(TAG, "Successfully signed in google account ${account?.displayName}")
            prefs.setAuthMethod(AuthMethod.GOOGLE)
            analytics.trackLogin(AuthMethod.GOOGLE)
            mLoginListener.onSuccessfulLogin()
        }
        catch (e: ApiException) {
            if (e.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED)
                return
            logError(TAG, "Failed to sign in with google: ${e.message}")
            snackbar.show(getString(R.string.sign_in_google_failure))
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
        button_sign_in_vk.setOnClickListener {
            VKSdk.login(requireActivity(), VKScope.FRIENDS, VKScope.OFFLINE)
        }
        button_sign_in_google.setOnClickListener {
            startActivityForResult(googleClient.signInIntent,
                GOOGLE_SIGN_IN_REQUEST_CODE
            )
        }
        link_sign_up.setOnClickListener {
            val intent = Intent(requireActivity(), SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logIn(email: String, password: String) {
        showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            hideProgress()
            if (task.isSuccessful) {
                val user = auth.currentUser
                logInfo(TAG, "uUer with email ${user?.email} successfully logged in")
                prefs.setAuthMethod(AuthMethod.EMAIL)
                analytics.trackLogin(AuthMethod.EMAIL)
                mLoginListener.onSuccessfulLogin()
            } else {
                logError(TAG, "Failed to login with email: ${task.exception}")
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

    companion object {
        private const val GOOGLE_SIGN_IN_REQUEST_CODE = 1
        private val TAG = LoginFragment::class.java.simpleName
    }
}
