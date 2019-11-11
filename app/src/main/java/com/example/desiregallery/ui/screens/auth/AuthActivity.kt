package com.example.desiregallery.ui.screens.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.transition.Slide
import com.example.desiregallery.R

class AuthActivity : AppCompatActivity(),
    IAuthFragmentManager {
    private var currFragment: BaseAuthFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val isLaunch: Boolean? = intent.extras?.getBoolean(EXTRA_IS_LAUNCH, false)
        if (isLaunch == null || isLaunch == true)
            setSplashScreenFragment(null, Slide(Gravity.START))
        else
            setLoginFragment(Slide(Gravity.START), Slide(Gravity.START))
    }

    override fun onBackPressed() {
        if (currFragment is SignupFragment)
            setLoginFragment(Slide(Gravity.START), Slide(Gravity.START))
        else
            super.onBackPressed()
    }

    override fun setSplashScreenFragment(enter: Slide?, exit: Slide?) {
        val splashFragment = SplashScreenFragment.createInstance(this).apply {
            enter?.let {
                enterTransition = it
            }
            exit?.let {
                exitTransition = it
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, splashFragment)
            .commit()
        currFragment = splashFragment
    }

    override fun setLoginFragment(enter: Slide?, exit: Slide?) {
        val loginFragment = LoginFragment.createInstance(this).apply {
            enter?.let {
                enterTransition = it
            }
            exit?.let {
                exitTransition = it
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, loginFragment)
            .commit()
        currFragment = loginFragment
    }

    override fun setSignupFragment(enter: Slide?, exit: Slide?) {
        val signupFragment = SignupFragment.createInstance(this).apply {
            enter?.let {
                enterTransition = it
            }
            exit?.let {
                exitTransition = it
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, signupFragment)
            .commit()
        currFragment = signupFragment
    }

    companion object {
        const val EXTRA_IS_LAUNCH = "is_launch"
    }
}
