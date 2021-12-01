package com.vitor238.popcorn

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class PopcornApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initTheme()
    }

    private fun initTheme() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val nightMode = preferences.getBoolean("pref_night_mode", true)
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}