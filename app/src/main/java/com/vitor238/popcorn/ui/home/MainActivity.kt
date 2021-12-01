package com.vitor238.popcorn.ui.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.vitor238.popcorn.R
import com.vitor238.popcorn.databinding.ActivityMainBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.preferences.main.SettingsActivity
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(toolbar = binding.toolbar.toolbarLogo, showBackButton = false)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.getFirestoreUser()

        profileViewModel.firestoreUserLiveData.observe(this) { user ->

            val settingsItem = menu?.findItem(R.id.menu_settings)
            Glide.with(this)
                .asDrawable()
                .load(user?.photoUrl)
                .circleCrop()
                .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_account_circle_24))
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        settingsItem?.icon = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}