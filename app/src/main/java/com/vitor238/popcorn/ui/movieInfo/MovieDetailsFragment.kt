package com.vitor238.popcorn.ui.movieInfo

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
import com.vitor238.popcorn.utils.setFormatedText

private const val MOVIE = "movie"

class MovieDetailsFragment : Fragment() {
    private var movie: Movie? = null
    private var _bindiding: FragmentMovieDetailsBinding? = null
    private val binding: FragmentMovieDetailsBinding
        get() = _bindiding!!

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
        _bindiding = FragmentMovieDetailsBinding.inflate(layoutInflater, container, false)

        if (movie?.tagline.isNullOrEmpty()) {
            binding.textTagline.visibility = View.GONE
        } else {
            binding.textTagline.setFormatedText(getString(R.string.tagline, movie?.tagline))
        }

        binding.textOrginalTitle.setFormatedText(
            getString(
                R.string.original_title,
                movie?.originalTitle
            )
        )
        binding.textOverview.setFormatedText(getString(R.string.overview, movie?.overview))
        binding.textGenres.setFormatedText(getGenresList())
        binding.textHomepage.setFormatedText(getString(R.string.homepage, movie?.homepage))
        binding.textProductionCompanies.setFormatedText(getProductionCompanies())
        binding.textProductionCountries.setFormatedText(getProductionCountries())
        binding.textReleaseDate.setFormatedText(getReleaseDate())

        binding.textHomepage.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(movie?.homepage)
            )
            startActivity(browserIntent)
        }

        return binding.root
    }


    private fun getGenresList(): String {
        val list = mutableListOf<String>()
        movie?.genres?.forEach {
            list.add(it.name)
        }
        return getString(R.string.genres, list.joinToString())
    }

    private fun getProductionCompanies(): String {
        val list = mutableListOf<String>()
        movie?.productionCompanies?.forEach {
            list.add(it.name)
        }
        return getString(R.string.production_companies, list.joinToString())
    }

    private fun getProductionCountries(): String {
        val list = mutableListOf<String>()
        movie?.productionCountries?.forEach {
            list.add(it.name)
        }
        return getString(R.string.production_countries, list.joinToString())
    }

    private fun getReleaseDate(): String {
        return getString(R.string.release_date, movie?.releaseDate)
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
        _bindiding = null
    }
}