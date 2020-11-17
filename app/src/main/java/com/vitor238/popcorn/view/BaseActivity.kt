package com.vitor238.popcorn.view

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