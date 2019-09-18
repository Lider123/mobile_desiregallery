package com.example.desiregallery.ui.fragments

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.desiregallery.MainApplication
import com.example.desiregallery.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        private val TAG = SettingsFragment::class.java.simpleName
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.pref_change_password_key) -> {
                val user = MainApplication.auth.currentUser
                user?.let {
                    MainApplication.auth.sendPasswordResetEmail(user.email!!).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i(TAG, "Message for password reset was sent to email ${user.email}")
                            Toast.makeText(requireActivity(), R.string.reset_password_sent, Toast.LENGTH_LONG).show()
                        }
                        else {
                            Log.i(TAG, "An error occurred while sending password reset email: ${task.exception?.message}")
                            Toast.makeText(requireActivity(), R.string.reset_password_error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
                true
            }
            else -> super.onPreferenceTreeClick(preference)
        }
    }
}
