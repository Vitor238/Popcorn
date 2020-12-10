package com.vitor238.popcorn.ui.serieinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vitor238.popcorn.R
import com.vitor238.popcorn.databinding.ActivitySerieInfoBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.utils.ApiStatus
import com.vitor238.popcorn.utils.BaseUrls
import jp.wasabeef.glide.transformations.BlurTransformation

class SerieInfoActivity : BaseActivity() {

    private lateinit var binding: ActivitySerieInfoBinding
    private var serieId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySerieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serieId = intent.extras?.getInt(SERIE_ID)

        val seriesViewModel = ViewModelProvider(this).get(SerieViewModel::class.java)
        seriesViewModel.getSerieInfo(serieId!!)

        seriesViewModel.status.observe(this) { status ->
            status?.let {
                when (it) {
                    ApiStatus.LOADING -> binding.viewFlipper.displayedChild = 0
                    ApiStatus.DONE -> binding.viewFlipper.displayedChild = 1
                    ApiStatus.ERROR -> binding.viewFlipper.displayedChild = 2
                }
            }
        }

        seriesViewModel.serieInfo.observe(this) { serie ->

            binding.toolbar.title = serie.name

            val adapter = TabsAdapter(supportFragmentManager)
            adapter.addFragment(
                SerieDetailsFragment.newInstance(serie),
                getString(R.string.details)
            )
            adapter.addFragment(
                SerieRecommendationsFragment.newInstance(serie.id),
                getString(R.string.more_like_this)
            )
            binding.viewPager.adapter = adapter
            binding.tabs.setupWithViewPager(binding.viewPager)

            Glide.with(this).load(
                BaseUrls.BASE_TMDB_IMG_URL_200 + serie.posterPath
            )
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(binding.imageCover)

            Glide.with(this).load(BaseUrls.BASE_TMDB_IMG_URL_200 + serie.posterPath)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(20, 3)))
                .placeholder(R.color.gray)
                .into(binding.appBarImage)
        }

        binding.toolbar.inflateMenu(R.menu.menu_favorite)
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        private const val SERIE_ID = "serieId"
        fun getStartIntent(context: Context, serieId: Int): Intent {
            return Intent(context, SerieInfoActivity::class.java).apply {
                putExtra(SERIE_ID, serieId)
            }
        }
    }
}