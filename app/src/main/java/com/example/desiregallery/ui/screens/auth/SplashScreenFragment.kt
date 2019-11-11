package com.example.desiregallery.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.Slide
import com.example.desiregallery.R
import com.example.desiregallery.data.prefs.IDGSharedPreferencesHelper
import com.example.desiregallery.ui.screens.MainActivity
import org.koin.android.ext.android.get

/**
 * @author babaetskv on 11.11.19
 */
class SplashScreenFragment private constructor(manager: IAuthFragmentManager): BaseAuthFragment(manager) {
    private val prefs: IDGSharedPreferencesHelper = get()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (prefs.hasAuthMethod())
            goToMainActivity()
        else {
            val r = Runnable(this::goToLoginActivity)
            Handler().postDelayed(r,
                TIMEOUT
            )
        }
    }

    private fun goToLoginActivity() {
        manager.setLoginFragment(Slide(Gravity.END), Slide(Gravity.START))
    }

    private fun goToMainActivity() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        private var TIMEOUT = 2500L

        fun createInstance(manager: IAuthFragmentManager) =
            SplashScreenFragment(manager)
    }
}