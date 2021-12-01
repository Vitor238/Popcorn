package com.vitor238.popcorn.ui.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.databinding.FragmentHomeBinding
import com.vitor238.popcorn.ui.home.home.movies.PopularMoviesAdapter
import com.vitor238.popcorn.ui.home.home.movies.PopularMoviesViewModel
import com.vitor238.popcorn.ui.home.home.series.PopularTvSeriesAdapter
import com.vitor238.popcorn.ui.home.home.series.PopularTvSeriesViewModel
import com.vitor238.popcorn.ui.home.home.trends.TrendsAdapter
import com.vitor238.popcorn.ui.home.home.trends.TrendsViewModel
import com.vitor238.popcorn.ui.movieinfo.MovieInfoActivity
import com.vitor238.popcorn.ui.tvserieinfo.TvSerieInfoActivity
import com.vitor238.popcorn.utils.Constants.MEDIA_TYPE_ALL
import com.vitor238.popcorn.utils.Constants.MEDIA_TYPE_MOVIE
import com.vitor238.popcorn.utils.Constants.MEDIA_TYPE_TV
import com.vitor238.popcorn.utils.Constants.TIME_WINDOW_WEEK
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint()
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val popularTvSeriesViewModel by viewModels<PopularTvSeriesViewModel>()
    private val popularMovieViewModel by viewModels<PopularMoviesViewModel>()
    private val trendsViewModel by viewModels<TrendsViewModel>()
    private lateinit var popularTvSeriesAdapter: PopularTvSeriesAdapter
    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var trendsAdapter: TrendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setTrendsRecyclerView()
        setPopularTvSeriesRecyclerView()
        observeViewModels()
        setPopularMoviesRecyclerView()
        return binding.root
    }

    private fun observeViewModels() {
        binding.viewFlipperTrends.displayedChild = 0
        trendsViewModel.getTrends(MEDIA_TYPE_ALL, TIME_WINDOW_WEEK)

        trendsViewModel.trends.observe(viewLifecycleOwner) { trends ->
            when (trends) {
                is NetworkResult.Success -> {
                    binding.viewFlipperTrends.displayedChild = 1
                    trendsAdapter.submitList(trends.value)
                }
                is NetworkResult.Error -> {
                    binding.viewFlipperTrends.displayedChild = 2
                }
            }
        }

        binding.viewFlipperTvSeries.displayedChild = 0
        popularTvSeriesViewModel.getPopularTVSeries()

        popularTvSeriesViewModel.popularTvSeries.observe(viewLifecycleOwner) { popularSeries ->
            when (popularSeries) {
                is NetworkResult.Success -> {
                    popularTvSeriesAdapter.submitList(popularSeries.value)
                    binding.viewFlipperTvSeries.displayedChild = 1
                }
                is NetworkResult.Error -> {
                    binding.viewFlipperTvSeries.displayedChild = 2
                }
            }
        }

        binding.viewFlipperMovies.displayedChild = 0
        popularMovieViewModel.getPopularMovies()

        popularMovieViewModel.popularMovies.observe(viewLifecycleOwner) { popularMovies ->
            when (popularMovies) {
                is NetworkResult.Success -> {
                    binding.viewFlipperMovies.displayedChild = 1
                    popularMoviesAdapter.submitList(popularMovies.value)
                }
                is NetworkResult.Error -> {
                    binding.viewFlipperMovies.displayedChild = 2
                }
            }
        }
    }

    private fun setTrendsRecyclerView() {
        trendsAdapter = TrendsAdapter { trend ->
            if (trend.mediaType == MEDIA_TYPE_TV) {
                openSeriesInfo(trend.id)
            } else if (trend.mediaType == MEDIA_TYPE_MOVIE) {
                openMovieInfo(trend.id)
            }
        }
        binding.recyclerTrends.setHasFixedSize(true)
        binding.recyclerTrends.adapter = trendsAdapter
    }

    private fun setPopularTvSeriesRecyclerView() {
        popularTvSeriesAdapter = PopularTvSeriesAdapter { serie ->
            serie.id?.let {
                openSeriesInfo(it)
            }
        }
        binding.recyclerTvSeries.setHasFixedSize(true)
        binding.recyclerTvSeries.adapter = popularTvSeriesAdapter
    }

    private fun setPopularMoviesRecyclerView() {
        popularMoviesAdapter = PopularMoviesAdapter { movie ->
            movie.id?.let {
                openMovieInfo(it)
            }

        }
        binding.recyclerMovies.setHasFixedSize(true)
        binding.recyclerMovies.adapter = popularMoviesAdapter
    }

    private fun openSeriesInfo(id: Int) {
        val intent = TvSerieInfoActivity.getStartIntent(requireActivity(), id)
        startActivity(intent)
    }

    private fun openMovieInfo(id: Int) {
        val intent = MovieInfoActivity.getStartIntent(requireActivity(), id)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}