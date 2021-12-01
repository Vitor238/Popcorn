package com.vitor238.popcorn.ui.base

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.vitor238.popcorn.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        //Pre api 23 alternative to android:windowLightStatusBar
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = ResourcesCompat.getColor(resources, R.color.material_gray, null)
        }
    }

    fun setupToolbar(
        toolbar: Toolbar,
        @StringRes
        titleIdRes: Int? = null,
        showBackButton: Boolean = false
    ) {

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(showBackButton)
        if (titleIdRes != null) {
            supportActionBar?.title = getString(titleIdRes)
        } else {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun showSnackBar(
        @NonNull view: View,
        @StringRes text: Int,
        @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT
    ) {
        Snackbar.make(view, text, duration)
            .show()
    }

    fun showSnackBar(
        @NonNull view: View,
        text: String,
        @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT
    ) {
        Snackbar.make(view, text, duration)
            .show()
    }
}