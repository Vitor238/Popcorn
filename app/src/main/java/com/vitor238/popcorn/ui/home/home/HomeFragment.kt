package com.vitor238.popcorn.ui.home.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vitor238.popcorn.databinding.FragmentHomeBinding
import com.vitor238.popcorn.ui.home.home.movies.PopularMoviesAdapter
import com.vitor238.popcorn.ui.home.home.movies.PopularMoviesViewModel
import com.vitor238.popcorn.ui.home.home.series.PopularSeriesAdapter
import com.vitor238.popcorn.ui.home.home.series.PopularSeriesViewModel
import com.vitor238.popcorn.ui.home.home.trends.TrendsAdapter
import com.vitor238.popcorn.ui.home.home.trends.TrendsViewModel
import com.vitor238.popcorn.utils.ApiStatus

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val trendsAdapter = TrendsAdapter()
    private lateinit var trendsViewModel: TrendsViewModel
    private lateinit var popularSeriesViewModel: PopularSeriesViewModel
    private val popularSeriesAdapter = PopularSeriesAdapter()
    private lateinit var popularMovieViewModel: PopularMoviesViewModel
    private val popularMoviesAdapter = PopularMoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.recyclerTrends.setHasFixedSize(true)
        binding.recyclerTrends.adapter = trendsAdapter

        binding.recyclerTvSeries.setHasFixedSize(true)
        binding.recyclerTvSeries.adapter = popularSeriesAdapter

        binding.recyclerMovies.setHasFixedSize(true)
        binding.recyclerMovies.adapter = popularMoviesAdapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        trendsViewModel = ViewModelProvider(this).get(TrendsViewModel::class.java)
        trendsViewModel.trends.observe(viewLifecycleOwner) { trends ->
            trendsAdapter.submitList(trends)
        }
        trendsViewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                when (it) {
                    ApiStatus.LOADING -> binding.viewFlipperTrends.displayedChild = 0
                    ApiStatus.DONE -> binding.viewFlipperTrends.displayedChild = 1
                    ApiStatus.ERROR -> binding.viewFlipperTrends.displayedChild = 2
                }
            }
        }

        popularSeriesViewModel = ViewModelProvider(this).get(PopularSeriesViewModel::class.java)
        popularSeriesViewModel.popularTVSeries.observe(viewLifecycleOwner) { popularSeries ->
            popularSeriesAdapter.submitList(popularSeries)
        }
        popularSeriesViewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                when (it) {
                    ApiStatus.LOADING -> binding.viewFlipperSeries.displayedChild = 0
                    ApiStatus.DONE -> binding.viewFlipperSeries.displayedChild = 1
                    ApiStatus.ERROR -> binding.viewFlipperSeries.displayedChild = 2
                }
            }
        }

        popularMovieViewModel = ViewModelProvider(this).get(PopularMoviesViewModel::class.java)
        popularMovieViewModel.popularMovies.observe(viewLifecycleOwner) { popularMovies ->
            popularMoviesAdapter.submitList(popularMovies)
            Log.i(TAG, "onActivityCreated: $popularMovies")
        }

        popularMovieViewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                when (it) {
                    ApiStatus.LOADING -> binding.viewFlipperMovies.displayedChild = 0
                    ApiStatus.DONE -> binding.viewFlipperMovies.displayedChild = 1
                    ApiStatus.ERROR -> binding.viewFlipperMovies.displayedChild = 2
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = HomeFragment()
        private val TAG = HomeFragment::class.simpleName
    }
}