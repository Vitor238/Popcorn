package com.vitor238.popcorn.ui.preferences.profile

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vitor238.popcorn.R
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel

class ProfilePreferences : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_profile, rootKey)
        val prefUserName = findPreference<Preference>("userName")

        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.getFirestoreUser()
        profileViewModel.firestoreUserLiveData.observe(this) { user ->

            val name = user?.name

            prefUserName?.title = name

            prefUserName?.setOnPreferenceClickListener {
                val changeNameDialogFragment = ChangeNameDialogFragment.newInstance(name)
                changeNameDialogFragment.show(
                    childFragmentManager,
                    changeNameDialogFragment.tag
                )
                true
            }
        }
    }
}
