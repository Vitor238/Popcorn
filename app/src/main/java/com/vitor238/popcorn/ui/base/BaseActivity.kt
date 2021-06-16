package com.vitor238.popcorn.ui.base

import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

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