package com.vitor238.popcorn.view

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import com.vitor238.popcorn.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(findViewById(R.id.toolbar))

        setupToolbar(toolbar = toolbar, titleIdRes = R.string.about, showBackButton = true)

        button_github.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/Vitor238/Popcorn")
            )
            startActivity(browserIntent)
        }

        image_the_movie_db.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.themoviedb.org/documentation/api")
            )
            startActivity(browserIntent)
        }

        showVersion()
    }

    private fun showVersion() {
        try {
            val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            text_version.text = getString(R.string.version, version)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}