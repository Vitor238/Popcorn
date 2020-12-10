package com.vitor238.popcorn.ui.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vitor238.popcorn.R
import com.vitor238.popcorn.databinding.ActivityMainBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.home.favorites.FavoritesFragment
import com.vitor238.popcorn.ui.home.home.HomeFragment
import com.vitor238.popcorn.ui.home.nowplaying.NowPlayingFragment
import com.vitor238.popcorn.ui.preferences.main.SettingsActivity
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(toolbar = binding.toolbar.toolbarLogo, showBackButton = false)

        openFragment(HomeFragment.newInstance())

        binding.navigation.setOnNavigationItemSelectedListener(getNavigationListener())
    }

    private fun getNavigationListener(): BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_home -> {
                    val homeFragment = HomeFragment.newInstance()
                    openFragment(homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_theaters -> {
                    val categoriesFragment = NowPlayingFragment.newInstance()
                    openFragment(categoriesFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_favorites -> {
                    val favoritesFragment = FavoritesFragment.newInstance()
                    openFragment(favoritesFragment)
                    return@OnNavigationItemSelectedListener true
                }
                else -> {
                    return@OnNavigationItemSelectedListener false
                }
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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