package com.vitor238.popcorn.ui.preferences.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vitor238.popcorn.R
import com.vitor238.popcorn.databinding.ActivitySettingsBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(
            toolbar = binding.toolbar,
            showBackButton = true,
            titleIdRes = R.string.settings
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner_settings, MainPreferences())
            .commit()

        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.getFirestoreUser()
        profileViewModel.firestoreUserLiveData.observe(this) { user ->
            if (user != null) {
                supportActionBar?.title = user.name
            } else {
                supportActionBar?.title = getString(R.string.settings)
            }

            Glide.with(this)
                .load(user?.photoUrl)
                .circleCrop()
                .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_account_circle_24))
                .into(binding.imageProfile)
        }
    }

    companion object {
        private val TAG = SettingsActivity::class.simpleName
    }
}
