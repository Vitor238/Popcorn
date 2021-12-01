package com.vitor238.popcorn.ui.movieinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.databinding.FragmentMovieRecommendationsBinding
import dagger.hilt.android.AndroidEntryPoint

private const val MOVIE_ID = "movieId"

@AndroidEntryPoint()
class MovieRecommendationsFragment : Fragment() {
    private var movieId: Int? = null
    private lateinit var movieRecommendationAdapter: MovieRecommendationAdapter
    private val movieRecommendationViewModel by viewModels<MovieRecommendationViewModel>()
    private var _binding: FragmentMovieRecommendationsBinding? = null
    private val binding: FragmentMovieRecommendationsBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt(MOVIE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieRecommendationsBinding.inflate(layoutInflater, container, false)

        movieRecommendationAdapter = MovieRecommendationAdapter {
            val intent = Intent(requireActivity(), MovieInfoActivity::class.java)
            intent.putExtra("movieId", it.id)
            startActivity(intent)
        }

        binding.content.recyclerRecommendations.setHasFixedSize(true)
        binding.content.recyclerRecommendations.adapter = movieRecommendationAdapter
        observeViewModels()
        return binding.root
    }

    private fun observeViewModels() {
        movieId?.let { id ->
            movieRecommendationViewModel.getRecommendations(id)
            binding.content.viewFlipper.displayedChild = 0
            movieRecommendationViewModel.movieRecommendation.observe(viewLifecycleOwner) { networkReuslt ->
                when (networkReuslt) {
                    is NetworkResult.Success -> {
                        movieRecommendationAdapter.submitList(networkReuslt.value)
                        binding.content.viewFlipper.displayedChild = 1
                    }
                    is NetworkResult.Error -> {
                        binding.content.viewFlipper.displayedChild = 2
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(movieId: Int) =
            MovieRecommendationsFragment().apply {
                arguments = Bundle().apply {
                    putInt(MOVIE_ID, movieId)
                }
            }
    }
}