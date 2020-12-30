package com.vitor238.popcorn.ui.preferences.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.vitor238.popcorn.R
import com.vitor238.popcorn.ui.preferences.about.AboutActivity
import com.vitor238.popcorn.ui.preferences.profile.EditProfileActivity
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModel
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModelFactory
import com.vitor238.popcorn.ui.welcome.WelcomeActivity
import com.vitor238.popcorn.utils.PreferencesUtils

class MainPreferences : PreferenceFragmentCompat() {
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
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val prefLogout = findPreference<Preference>("pref_logout")
        val prefEditProfile = findPreference<Preference>("pref_edit_profile")
        val prefAbout = findPreference<Preference>("pref_about")
        val prefNightMode = findPreference<SwitchPreference>("pref_night_mode")

        val loggedInViewModelFactory = LoggedInViewModelFactory(activity?.application!!)
        val loggedInViewModel = ViewModelProvider(this, loggedInViewModelFactory)
            .get(LoggedInViewModel::class.java)

        loggedInViewModel.loggedOutMutableLiveData.observe(this) { loggedOut ->
            if (loggedOut) {
                val intent = Intent(activity, WelcomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        prefLogout?.setOnPreferenceClickListener {
            loggedInViewModel.logOut()
            true
        }

        prefEditProfile?.setOnPreferenceClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
            true
        }

        prefAbout?.setOnPreferenceClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            startActivity(intent)
            true
        }

        prefNightMode?.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue == true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }
    }
}
