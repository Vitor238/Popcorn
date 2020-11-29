package com.vitor238.popcorn.ui.preferences.about

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import com.vitor238.popcorn.R
import com.vitor238.popcorn.databinding.ActivityAboutBinding
import com.vitor238.popcorn.ui.base.BaseActivity

class AboutActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        setupToolbar(toolbar = binding.toolbar, titleIdRes = R.string.about, showBackButton = true)

        binding.buttonGithub.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/Vitor238/Popcorn")
            )
            startActivity(browserIntent)
        }

        binding.imageTheMovieDb.setOnClickListener {
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
            binding.textVersion.text = getString(R.string.version, version)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}