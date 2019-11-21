package com.example.desiregallery.ui.screens

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R
import com.example.desiregallery.ui.widgets.SnackbarWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {
    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var snackbar: SnackbarWrapper

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        MainApplication.appComponent.inject(this)
        addPreferencesFromResource(R.xml.prefs)
        auth.currentUser ?: run {
            val resetPreference =
                findPreference<Preference>(getString(R.string.pref_reset_password_key))
            preferenceScreen.removePreference(resetPreference)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackbar = SnackbarWrapper(view)
    }

    override fun onPreferenceTreeClick(preference: Preference) = when (preference.key) {
        getString(R.string.pref_reset_password_key) -> {
            val user = auth.currentUser
            user?.let {
                AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.reset_password)
                    .setMessage(R.string.reset_password_message)
                    .setPositiveButton(R.string.yes) { _, _ -> sendResetPasswordEmail(it) }
                    .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
                    .create()
                    .show()
            } ?: snackbar.show(getString(R.string.reset_password_unable))
            true
        }
        getString(R.string.pref_about_key) -> {
            AboutDialog(requireContext()).show()
            true
        }
        else -> super.onPreferenceTreeClick(preference)
    }


    private fun sendResetPasswordEmail(user: FirebaseUser) {
        auth.sendPasswordResetEmail(user.email!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.i("Message for password reset was sent to email ${user.email}")
                snackbar.show(getString(R.string.reset_password_sent))
            } else {
                Timber.e(task.exception, "An error occurred while sending password reset email")
                snackbar.show(getString(R.string.reset_password_error))
            }
        }
    }
}
