package com.example.desiregallery.ui.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.desiregallery.R
import com.example.desiregallery.logging.logInfo
import com.example.desiregallery.ui.dialogs.AboutDialog
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class SettingsFragment : PreferenceFragmentCompat() {
    private val auth: FirebaseAuth by inject()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.pref_change_password_key) -> {
                val user = auth.currentUser
                user?.let {
                    auth.sendPasswordResetEmail(user.email!!).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            logInfo(TAG, "Message for password reset was sent to email ${user.email}")
                            Toast.makeText(requireActivity(), R.string.reset_password_sent, Toast.LENGTH_LONG).show()
                        }
                        else {
                            logInfo(TAG, "An error occurred while sending password reset email: ${task.exception?.message}")
                            Toast.makeText(requireActivity(), R.string.reset_password_error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
                true
            }
            getString(R.string.pref_about_key) -> {
                AboutDialog(requireContext()).show()
                true
            }
            else -> super.onPreferenceTreeClick(preference)
        }
    }

    companion object {
        private val TAG = SettingsFragment::class.java.simpleName
    }
}
