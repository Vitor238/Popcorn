package com.vitor238.popcorn.ui.movieinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vitor238.popcorn.databinding.FragmentMovieRecommendationsBinding
import com.vitor238.popcorn.utils.ApiStatus

private const val MOVIE_ID = "movieId"

class MovieRecommendationsFragment : Fragment() {
    private var movieId: Int? = null
    private lateinit var movieRecommendationAdapter: MovieRecommendationAdapter
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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val movieRecommendationViewModel = ViewModelProvider(this)
            .get(MovieRecommendationViewModel::class.java)

        movieId?.let { id ->
            movieRecommendationViewModel.getRecommendadtions(id)

            movieRecommendationViewModel.movieRecommendation.observe(viewLifecycleOwner) { recommendations ->
                movieRecommendationAdapter.submitList(recommendations)
            }

            movieRecommendationViewModel.status.observe(viewLifecycleOwner) { status ->
                status?.let {
                    when (it) {
                        ApiStatus.LOADING -> binding.content.viewFlipper.displayedChild = 0
                        ApiStatus.DONE -> binding.content.viewFlipper.displayedChild = 1
                        ApiStatus.ERROR -> binding.content.viewFlipper.displayedChild = 2
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