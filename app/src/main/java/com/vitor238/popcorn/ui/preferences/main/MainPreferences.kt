package com.vitor238.popcorn.ui.preferences.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vitor238.popcorn.R
import com.vitor238.popcorn.ui.preferences.about.AboutActivity
import com.vitor238.popcorn.ui.preferences.profile.EditProfileActivity
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModel
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModelFactory
import com.vitor238.popcorn.ui.welcome.WelcomeActivity

class MainPreferences : PreferenceFragmentCompat() {
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
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val prefLogout = findPreference<Preference>("logout")
        val prefEditProfile = findPreference<Preference>("editProfile")
        val prefAbout = findPreference<Preference>("about")

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

    }
}
