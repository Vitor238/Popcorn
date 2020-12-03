package com.vitor238.popcorn.ui.serieInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.serie.Networks
import com.vitor238.popcorn.data.model.serie.Serie
import com.vitor238.popcorn.databinding.FragmentSerieDetailsBinding
import com.vitor238.popcorn.utils.setFormatedText

private const val SERIE = "serie"

class SerieDetailsFragment : Fragment() {

    private var serie: Serie? = null
    private var _binding: FragmentSerieDetailsBinding? = null
    private val binding: FragmentSerieDetailsBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serie = it.getParcelable(SERIE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSerieDetailsBinding.inflate(layoutInflater)
        binding.textOverview.setFormatedText(getString(R.string.overview, serie?.overview))
        binding.textOriginalName.setFormatedText(
            getString(
                R.string.original_name,
                serie?.originalName
            )
        )
        binding.textGenres.setFormatedText(getGenresList())
        binding.textCreatedBy.setFormatedText(getAuthors())
        binding.textInProdution.setFormatedText(getInProduction())
        binding.textFirstAirDate.setFormatedText(getFirstAirDate())
        binding.textLastAirDate.setFormatedText(getLastAirDate())
        binding.textNetworks.setFormatedText(getNetworks(serie?.networks))
        binding.textNumberOfEpisodes.setFormatedText(
            getString(
                R.string.number_of_episodes,
                serie?.numberOfEpisodes
            )
        )
        binding.textNumberOfSeasons.setFormatedText(
            getString(R.string.number_of_seasons, serie?.numberOfSeasons)
        )
        binding.textOriginCoutry.setFormatedText(getOriginCountries())
        binding.textProductionCompanies.setFormatedText(getProductionCompanies())
        return binding.root
    }

    private fun getAuthors(): String {
        val list = mutableListOf<String>()
        serie?.createdBy?.forEach {
            list.add(it.name)
        }
        return getString(R.string.created_by, list.joinToString())
    }

    private fun getGenresList(): String {
        val list = mutableListOf<String>()
        serie?.genres?.forEach {
            list.add(it.name)
        }
        return getString(R.string.genres, list.joinToString())
    }

    private fun getInProduction(): String {
        val inProduction = serie?.inProduction
        val value = when {
            inProduction == null -> {
                getString(R.string.error)
            }
            inProduction -> {
                getString(R.string.yes)
            }
            else -> {
                getString(R.string.no)
            }
        }
        return getString(R.string.in_prodution, value)
    }

    private fun getLastAirDate(): String {
        return getString(
            R.string.last_air_date,
            serie?.lastAirDate
        )
    }

    private fun getFirstAirDate(): String {
        return getString(
            R.string.first_air_date,
            serie?.firstAirDate
        )
    }

    private fun getNetworks(networksList: List<Networks>?): String {
        val list = mutableListOf<String>()
        networksList?.forEach {
            list.add(it.name)
        }
        return getString(R.string.networks, list.joinToString())
    }

    private fun getOriginCountries(): String {
        return getString(R.string.origin_country, serie?.originCountry?.joinToString())
    }

    private fun getProductionCompanies(): String {
        val list = mutableListOf<String>()
        serie?.productionCompanies?.forEach {
            list.add(it.name)
        }
        return getString(R.string.production_companies, list.joinToString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(serie: Serie) =
            SerieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SERIE, serie)
                }
            }
    }
}