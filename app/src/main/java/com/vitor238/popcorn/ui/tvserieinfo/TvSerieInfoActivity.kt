package com.vitor238.popcorn.ui.tvserieinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.model.Favorite
import com.vitor238.popcorn.data.model.tvserie.TvSerie
import com.vitor238.popcorn.databinding.ActivityTvSerieInfoBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.viewmodel.FavoritesViewModel
import com.vitor238.popcorn.ui.viewmodel.FavoritesViewModelFactory
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModel
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModelFactory
import com.vitor238.popcorn.utils.Constants
import com.vitor238.popcorn.utils.Constants.MEDIA_TYPE_TV
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.BlurTransformation

@AndroidEntryPoint
class TvSerieInfoActivity : BaseActivity() {

    private lateinit var binding: ActivityTvSerieInfoBinding
    private var serieId: Int? = null
    private lateinit var newFavorite: Favorite
    private lateinit var favoritesViewModel: FavoritesViewModel
    private val seriesViewModel by viewModels<TvSerieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvSerieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serieId = intent.extras?.getInt(SERIE_ID)

        observeViewModel()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_white)
    }

    private fun observeViewModel() {
        binding.viewFlipper.displayedChild = 0
        seriesViewModel.getSerieInfo(serieId!!)

        seriesViewModel.serieInfo.observe(this) { networkResult ->
            when (networkResult) {
                is NetworkResult.Success -> {
                    binding.viewFlipper.displayedChild = 1
                    val serie = networkResult.value
                    showDetails(serie)
                }
                is NetworkResult.Error -> {
                    binding.viewFlipper.displayedChild = 2
                }
            }
        }
    }


    private fun showDetails(serie: TvSerie) {
        setupTabLayout(serie)

        binding.toolbar.title = serie.name

        Glide.with(this).load(Constants.BASE_TMDB_IMG_URL_200 + serie.posterPath)
            .placeholder(R.drawable.ic_movie_placeholder)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
            .into(binding.imageCover)

        Glide.with(this).load(Constants.BASE_TMDB_IMG_URL_200 + serie.posterPath)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(20, 3)))
            .placeholder(R.color.placeholder_bg_color)
            .into(binding.appBarImage)

        newFavorite = Favorite(
            mediaType = MEDIA_TYPE_TV,
            mediaId = serie.id,
            title = serie.name ?: serie.originalName,
            posterPath = serie.posterPath
        )
        verifyLogin()
    }

    private fun setupTabLayout(serie: TvSerie) {

        binding.viewPager.adapter = TvSeriesPagerAdapter(this, serie)

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            if (position == 0) {
                tab.setText(R.string.details)
            } else {
                tab.setText(R.string.more_like_this)
            }
        }.attach()
    }

    private fun verifyLogin() {
        val loggedInViewModelFactory = LoggedInViewModelFactory(application)
        val loggedInViewModel = ViewModelProvider(this, loggedInViewModelFactory)
            .get(LoggedInViewModel::class.java)

        loggedInViewModel.firebaseUserMutableLiveData.observe(this) { firebaseUser ->
            firebaseUser?.let {
                getFavoriteState(it.uid)
            }
        }
    }

    private fun getFavoriteState(userId: String) {

        binding.toolbar.menu.clear()
        binding.toolbar.inflateMenu(R.menu.menu_favorite)

        val favoriteViewModelFactory = FavoritesViewModelFactory(userId)
        favoritesViewModel = ViewModelProvider(this, favoriteViewModelFactory)
            .get(FavoritesViewModel::class.java)

        favoritesViewModel.checkFavorite(newFavorite)

        favoritesViewModel.favorite.observe(this) { favoriteSaved ->
            Log.i(TAG, "Favorite: $favoriteSaved")


            val item = binding.toolbar.menu.findItem(R.id.action_save_to_favorites)

            if (favoriteSaved == null) {
                item.setIcon(R.drawable.ic_baseline_star_white_outline_24)
            } else {
                item.setIcon(R.drawable.ic_baseline_star_white_24)
            }

            setupFavoriteButtonClick(favoriteSaved)

        }
    }

    private fun setupFavoriteButtonClick(favoriteSaved: Favorite?) {
        binding.toolbar.setOnMenuItemClickListener {
            if (favoriteSaved != null) {
                favoritesViewModel.removeFavorite(favoriteSaved)
            } else {
                favoritesViewModel.saveFavorite(newFavorite)
            }
            true
        }
    }

    companion object {
        private val TAG = TvSerieInfoActivity::class.simpleName
        private const val SERIE_ID = "serieId"
        fun getStartIntent(context: Context, serieId: Int): Intent {
            return Intent(context, TvSerieInfoActivity::class.java).apply {
                putExtra(SERIE_ID, serieId)
            }
        }
    }
}