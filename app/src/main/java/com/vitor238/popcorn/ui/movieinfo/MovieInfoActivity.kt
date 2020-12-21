package com.vitor238.popcorn.ui.movieinfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.Favorite
import com.vitor238.popcorn.databinding.ActivityMovieInfoBinding
import com.vitor238.popcorn.ui.serieinfo.TabsAdapter
import com.vitor238.popcorn.utils.ApiStatus
import com.vitor238.popcorn.utils.BaseUrls
import com.vitor238.popcorn.utils.MediaTypes
import jp.wasabeef.glide.transformations.BlurTransformation

class MovieInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieInfoBinding
    private var movieId: Int? = null
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var favorite: Favorite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.extras?.getInt(MOVIE_ID)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        observeViewModel()

        binding.content.toolbar.inflateMenu(R.menu.menu_favorite)
        binding.content.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.content.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun observeViewModel() {
        movieViewModel.getMovieInfo(movieId!!)

        movieViewModel.status.observe(this) { status ->
            status?.let {
                when (it) {
                    ApiStatus.LOADING -> binding.content.viewFlipper.displayedChild = 0
                    ApiStatus.DONE -> binding.content.viewFlipper.displayedChild = 1
                    ApiStatus.ERROR -> binding.content.viewFlipper.displayedChild = 2
                }
            }
        }

        movieViewModel.movieInfo.observe(this) { movie ->

            binding.content.toolbar.title = movie.title

            val adapter = TabsAdapter(supportFragmentManager)
            adapter.addFragment(
                MovieDetailsFragment.newInstance(movie),
                getString(R.string.details)
            )
            adapter.addFragment(
                MovieRecommendationsFragment.newInstance(movie.id),
                getString(R.string.more_like_this)
            )
            binding.content.viewPager.adapter = adapter
            binding.content.tabs.setupWithViewPager(binding.content.viewPager)

            Glide.with(this).load(
                BaseUrls.BASE_TMDB_IMG_URL_200 + movie.posterPath
            ).placeholder(R.drawable.ic_movie_placeholder)
                .into(binding.content.imageCover)

            Glide.with(this).load(BaseUrls.BASE_TMDB_IMG_URL_200 + movie.posterPath)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(20, 3)))
                .placeholder(R.color.gray)
                .into(binding.content.appBarImage)

            favorite = Favorite(
                mediaType = MediaTypes.MOVIE,
                mediaId = movie.id,
                title = movie.title ?: movie.originalTitle,
                posterPath = movie.posterPath
            )

            movieViewModel.checkFavorite(favorite)
        }

        movieViewModel.favorite.observe(this) { favoriteSaved ->
            Log.i(TAG, "onCreate: $favoriteSaved")

            val item = binding.content.toolbar.menu.findItem(R.id.action_save_to_favorites)

            if (favoriteSaved != null) {
                item.setIcon(R.drawable.ic_baseline_star_24)
                binding.content.toolbar.setOnMenuItemClickListener {
                    movieViewModel.removeFavorite(favoriteSaved)
                    Log.i(TAG, "onCreate: REMOVE!")
                    true
                }
            } else {
                item.setIcon(R.drawable.ic_baseline_star_outline_24)
                binding.content.toolbar.setOnMenuItemClickListener {
                    movieViewModel.saveFavorite(favorite)
                    Log.i(TAG, "onCreate: ADD!")
                    true
                }
            }
        }
    }

    companion object {
        private const val MOVIE_ID = "movieId"
        private val TAG = MovieInfoActivity::class.simpleName
        fun getStartIntent(context: Context, movieId: Int): Intent {
            return Intent(context, MovieInfoActivity::class.java).apply {
                putExtra(MOVIE_ID, movieId)
            }
        }
    }
}