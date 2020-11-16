package com.vitor238.popcorn.view

import android.content.Context
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class BaseActivity : AppCompatActivity() {

    fun setupToolbar(toolbar: Toolbar,
                               @StringRes
                               titleIdRes: Int? = null,
                               showBackButton: Boolean = false) {

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
}