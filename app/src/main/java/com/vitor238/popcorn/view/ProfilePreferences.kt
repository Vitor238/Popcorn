package com.vitor238.popcorn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vitor238.popcorn.R
import com.vitor238.popcorn.viewmodel.ProfileViewModel

class ProfilePreferences : PreferenceFragmentCompat() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.setBackgroundColor(ContextCompat.getColor(view.context, R.color.black))
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
