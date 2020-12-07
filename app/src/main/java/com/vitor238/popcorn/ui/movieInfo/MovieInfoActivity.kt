package com.vitor238.popcorn.ui.movieInfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vitor238.popcorn.R
import com.vitor238.popcorn.databinding.ActivityMovieInfoBinding
import com.vitor238.popcorn.ui.serieInfo.TabsAdapter
import com.vitor238.popcorn.utils.ApiStatus
import com.vitor238.popcorn.utils.BaseUrls
import jp.wasabeef.glide.transformations.BlurTransformation

class MovieInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieInfoBinding
    private var movieId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieId = intent.extras?.getInt("movieId")

        val movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
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
            ).into(binding.content.imageCover)

            Glide.with(this).load(BaseUrls.BASE_TMDB_IMG_URL_200 + movie.posterPath)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(20, 3)))
                .into(binding.content.appBarImage)
        }

        binding.content.toolbar.inflateMenu(R.menu.menu_favorite)
        binding.content.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.content.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}