package com.vitor238.popcorn.ui.base

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.vitor238.popcorn.R
import kotlin.math.abs

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

    fun setupAppbar(toolbar: Toolbar, appBar: AppBarLayout) {
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                // Collapsed
                toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            } else {
                // Expanded
                toolbar.setNavigationIcon(R.drawable.ic_circle_back_arrow)
            }
        })
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}