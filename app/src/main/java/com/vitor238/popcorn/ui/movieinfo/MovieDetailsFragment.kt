package com.vitor238.popcorn.ui.movieinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.movie.Movie
import com.vitor238.popcorn.databinding.FragmentMovieDetailsBinding
import com.vitor238.popcorn.utils.LocaleUtils
import com.vitor238.popcorn.utils.setDetails

private const val MOVIE = "movie"

class MovieDetailsFragment : Fragment() {
    private var movie: Movie? = null
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding: FragmentMovieDetailsBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getParcelable(MOVIE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailsBinding.inflate(layoutInflater, container, false)

        binding.textTagline.setDetails(R.string.tagline, movie?.tagline)
        binding.textOriginalTitle.setDetails(R.string.original_title, movie?.originalTitle)
        binding.textOverview.setDetails(R.string.overview, movie?.overview)
        binding.textGenres.setDetails(R.string.genres, getGenresList())
        binding.textHomepage.setDetails(R.string.homepage, movie?.homepage)
        binding.textProductionCompanies.setDetails(
            R.string.production_companies,
            getProductionCompanies()
        )
        binding.textProductionCountries.setDetails(
            R.string.production_countries,
            getProductionCountries()
        )
        binding.textReleaseDate.setDetails(
            R.string.release_date,
            LocaleUtils.parseDate(movie?.releaseDate)
        )

        binding.textHomepage.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(movie?.homepage)
            )
            startActivity(browserIntent)
        }

        return binding.root
    }


    private fun getGenresList(): String? {
        return if (movie?.genres != null) {
            val list = mutableListOf<String>()
            movie?.genres?.forEach {
                list.add(it.name)
            }
            list.joinToString()
        } else {
            null
        }

    }

    private fun getProductionCompanies(): String? {
        return if (movie?.productionCompanies != null) {
            val list = mutableListOf<String>()
            movie?.productionCompanies?.forEach {
                list.add(it.name)
            }
            list.joinToString()
        } else {
            null
        }
    }

    private fun getProductionCountries(): String? {
        return if (movie?.productionCountries != null) {
            val list = mutableListOf<String>()
            movie?.productionCountries?.forEach {
                list.add(it.name)
            }
            list.joinToString()
        } else {
            null
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(movie: Movie) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MOVIE, movie)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}