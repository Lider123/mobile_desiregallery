package com.example.desiregallery.ui.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.desiregallery.R
import com.example.desiregallery.ui.dialogs.ChangePasswordDialog


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.pref_change_password_key) -> {
                ChangePasswordDialog(activity!!).show()
                true
            }
            else -> {
                super.onPreferenceTreeClick(preference)
            }
        }
    }
}
