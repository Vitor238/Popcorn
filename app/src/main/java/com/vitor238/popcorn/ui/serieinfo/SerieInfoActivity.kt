package com.vitor238.popcorn.ui.serieinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.Favorite
import com.vitor238.popcorn.databinding.ActivitySerieInfoBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.utils.ApiStatus
import com.vitor238.popcorn.utils.BaseUrls
import com.vitor238.popcorn.utils.MediaTypes
import jp.wasabeef.glide.transformations.BlurTransformation

class SerieInfoActivity : BaseActivity() {

    private lateinit var binding: ActivitySerieInfoBinding
    private var serieId: Int? = null
    private lateinit var seriesViewModel: SerieViewModel
    private lateinit var favorite: Favorite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySerieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serieId = intent.extras?.getInt(SERIE_ID)
        seriesViewModel = ViewModelProvider(this).get(SerieViewModel::class.java)

        observeViewModel()

        binding.toolbar.inflateMenu(R.menu.menu_favorite)
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun observeViewModel() {
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

            Glide.with(this).load(BaseUrls.BASE_TMDB_IMG_URL_200 + serie.posterPath)
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(binding.imageCover)

            Glide.with(this).load(BaseUrls.BASE_TMDB_IMG_URL_200 + serie.posterPath)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(20, 3)))
                .placeholder(R.color.gray)
                .into(binding.appBarImage)

            favorite = Favorite(
                mediaType = MediaTypes.TV,
                mediaId = serie.id,
                title = serie.name ?: serie.originalName,
                posterPath = serie.posterPath
            )
            seriesViewModel.checkFavorite(favorite)
        }



        seriesViewModel.favorite.observe(this) { favoriteSaved ->
            Log.i(TAG, "onCreate: $favoriteSaved")

            val item = binding.toolbar.menu.findItem(R.id.action_save_to_favorites)

            if (favoriteSaved != null) {
                item.setIcon(R.drawable.ic_baseline_star_24)
                binding.toolbar.setOnMenuItemClickListener {
                    seriesViewModel.removeFavorite(favoriteSaved)
                    Log.i(TAG, "onCreate: REMOVE!")
                    true
                }
            } else {
                item.setIcon(R.drawable.ic_baseline_star_outline_24)
                binding.toolbar.setOnMenuItemClickListener {
                    seriesViewModel.saveFavorite(favorite)
                    Log.i(TAG, "onCreate: ADD!")
                    true
                }
            }
        }

    }

    companion object {
        private const val SERIE_ID = "serieId"
        private val TAG = SerieInfoActivity::class.simpleName
        fun getStartIntent(context: Context, serieId: Int): Intent {
            return Intent(context, SerieInfoActivity::class.java).apply {
                putExtra(SERIE_ID, serieId)
            }
        }
    }
}