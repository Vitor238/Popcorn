package com.vitor238.popcorn.ui.preferences.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vitor238.popcorn.R
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel
import com.vitor238.popcorn.utils.PreferencesUtils

class ProfilePreferences : PreferenceFragmentCompat() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        PreferencesUtils.setupBackgroundColor(view)
        return view
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_profile, rootKey)
        val prefUserName = findPreference<Preference>("userName")

        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.getFirestoreUser()
        profileViewModel.firestoreUserLiveData.observe(this) { user ->
            prefUserName?.title = user?.name
        }

        prefUserName?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                profileViewModel.updateFirestoreName(newValue.toString())
                profileViewModel.updateAuthName(newValue.toString())
                true
            }
    }
}
