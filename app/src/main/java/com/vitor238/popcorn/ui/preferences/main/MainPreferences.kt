package com.vitor238.popcorn.ui.preferences.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.recyclerview.widget.RecyclerView
import com.vitor238.popcorn.R
import com.vitor238.popcorn.ui.preferences.about.AboutActivity
import com.vitor238.popcorn.ui.preferences.profile.EditProfileActivity
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModel
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModelFactory
import com.vitor238.popcorn.ui.welcome.WelcomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPreferences : PreferenceFragmentCompat() {

    private val prefLogout by lazy { findPreference<Preference>("pref_logout") }
    private val prefEditProfile by lazy { findPreference<Preference>("pref_edit_profile") }
    private val prefAbout by lazy { findPreference<Preference>("pref_about") }
    private val prefNightMode by lazy { findPreference<SwitchPreference>("pref_night_mode") }
    private lateinit var loggedInViewModel: LoggedInViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val loggedInViewModelFactory = LoggedInViewModelFactory(activity?.application!!)
        loggedInViewModel = ViewModelProvider(this, loggedInViewModelFactory)
            .get(LoggedInViewModel::class.java)

        loggedInViewModel.loggedOutMutableLiveData.observe(viewLifecycleOwner) { loggedOut ->
            if (loggedOut) {
                val intent = Intent(activity, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        loggedInViewModel.firebaseUserMutableLiveData.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                prefEditProfile?.isVisible = true
            }
        }
    }

    override fun onCreateRecyclerView(
        inflater: LayoutInflater?,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): RecyclerView {
        val view = super.onCreateRecyclerView(inflater, parent, savedInstanceState)
        view.itemAnimator = null
        view.layoutAnimation = null
        return view
    }
}
