package com.vitor238.popcorn.view

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.vitor238.popcorn.R
import com.vitor238.popcorn.viewmodel.LoggedInViewModel
import com.vitor238.popcorn.viewmodel.LoggedInViewModelFactory
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupToolbar(toolbar = toolbar as Toolbar, showBackButton = true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.conteiner_settings, MainPreferences())
            .commit()

        val loggedInViewModelFactory = LoggedInViewModelFactory(application)
        val loggedInViewModel = ViewModelProvider(this, loggedInViewModelFactory)
            .get(LoggedInViewModel::class.java)

        loggedInViewModel.userMutableLiveData.observe(this) {
            if (it != null) {
                text_email.text = it.displayName
                Glide.with(this).load(it.photoUrl).circleCrop().into(image_profile)
            }
        }
    }

    companion object {
        private val TAG = SettingsActivity::class.simpleName
    }
}
