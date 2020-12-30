package com.vitor238.popcorn.ui.serieinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.Favorite
import com.vitor238.popcorn.databinding.ActivitySerieInfoBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.viewmodel.FavoritesViewModel
import com.vitor238.popcorn.ui.viewmodel.FavoritesViewModelFactory
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModel
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModelFactory
import com.vitor238.popcorn.utils.ApiStatus
import com.vitor238.popcorn.utils.BaseUrls
import com.vitor238.popcorn.utils.MediaTypes
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlin.math.abs

class SerieInfoActivity : BaseActivity() {

    private lateinit var binding: ActivitySerieInfoBinding
    private var serieId: Int? = null
    private lateinit var newFavorite: Favorite
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySerieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serieId = intent.extras?.getInt(SERIE_ID)

        getSerieInfo()

        setupAppbar(binding.toolbar, binding.appbar)
    }

    private fun getSerieInfo() {
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

            Glide.with(this).load(BaseUrls.BASE_TMDB_IMG_URL_200 + serie.posterPath)
                .placeholder(R.drawable.ic_movie_placeholder)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(4)))
                .into(binding.imageCover)

            Glide.with(this).load(BaseUrls.BASE_TMDB_IMG_URL_200 + serie.posterPath)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(20, 3)))
                .placeholder(R.color.gray)
                .into(binding.appBarImage)

            newFavorite = Favorite(
                mediaType = MediaTypes.TV,
                mediaId = serie.id,
                title = serie.name ?: serie.originalName,
                posterPath = serie.posterPath
            )
            verifyLogin()
        }
    }

    private fun verifyLogin() {
        val loggedInViewModelFactory = LoggedInViewModelFactory(application)
        val loggedInViewModel = ViewModelProvider(this, loggedInViewModelFactory)
            .get(LoggedInViewModel::class.java)

        loggedInViewModel.firebaseUserMutableLiveData.observe(this) { firesbaseUser ->
            firesbaseUser?.let {
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

            setupFavoriteButtonClick(favoriteSaved)

            val item = binding.toolbar.menu.findItem(R.id.action_save_to_favorites)

            binding.appbar
                .addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                    val isCollapsed = abs(verticalOffset) == appBarLayout.totalScrollRange
                    updateFavoriteIcon(favoriteSaved, isCollapsed, item)
                })
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

    private fun updateFavoriteIcon(
        favoriteSaved: Favorite?,
        isCollapsed: Boolean,
        menuItem: MenuItem
    ) {
        if (favoriteSaved != null && isCollapsed) {
            menuItem.setIcon(R.drawable.ic_baseline_star_24)
        } else if (favoriteSaved != null && !isCollapsed) {
            menuItem.setIcon(R.drawable.ic_circle_star)
        } else if (favoriteSaved == null && isCollapsed) {
            menuItem.setIcon(R.drawable.ic_baseline_star_outline_24)
        } else {
            menuItem.setIcon(R.drawable.ic_circle_star_outline)
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