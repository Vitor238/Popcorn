package com.vitor238.popcorn.ui.serieinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.serie.Serie
import com.vitor238.popcorn.databinding.FragmentSerieDetailsBinding
import com.vitor238.popcorn.utils.LocaleUtils
import com.vitor238.popcorn.utils.setTextOrHide

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

        binding.textOverview.setTextOrHide(R.string.overview, serie?.overview)
        binding.textOriginalName.setTextOrHide(R.string.original_name, serie?.originalName)
        binding.textGenres.setTextOrHide(R.string.genres, getGenresList())
        binding.textCreatedBy.setTextOrHide(R.string.created_by, getAuthors())
        binding.textInProduction.setTextOrHide(R.string.in_production, getInProduction())
        binding.textFirstAirDate.setTextOrHide(
            R.string.first_air_date,
            LocaleUtils.parseDate(serie?.firstAirDate)
        )
        binding.textLastAirDate.setTextOrHide(
            R.string.last_air_date,
            LocaleUtils.parseDate(serie?.lastAirDate)
        )
        binding.textNetworks.setTextOrHide(R.string.networks, getNetworks())
        binding.textNumberOfEpisodes.setTextOrHide(
            R.string.number_of_episodes,
            serie?.numberOfEpisodes
        )
        binding.textNumberOfSeasons.setTextOrHide(
            R.string.number_of_seasons,
            serie?.numberOfSeasons
        )
        binding.textOriginCountry.setTextOrHide(R.string.origin_country, getOriginCountries())
        binding.textProductionCompanies.setTextOrHide(
            R.string.production_companies,
            getProductionCompanies()
        )

        return binding.root
    }

    private fun getAuthors(): String? {
        return if (serie?.createdBy == null) {
            val list = mutableListOf<String>()

            serie?.createdBy?.forEach {
                list.add(it.name)
            }
            return getString(R.string.created_by, list.joinToString())
        } else {
            null
        }
    }

    private fun getGenresList(): String? {
        return if (serie?.genres != null) {
            val list = mutableListOf<String>()
            serie?.genres?.forEach {
                list.add(it.name)
            }
            list.joinToString()
        } else {
            null
        }


    }

    private fun getInProduction(): String? {
        return when (serie?.inProduction) {
            null -> null
            true -> getString(R.string.yes)
            else -> getString(R.string.no)
        }
    }

    private fun getNetworks(): String? {
        return if (serie?.networks != null) {
            val list = mutableListOf<String>()
            serie?.networks?.forEach {
                list.add(it.name)
            }
            list.joinToString()
        } else {
            null
        }

    }

    private fun getOriginCountries(): String? {
        return if (serie?.originCountry != null) {
            serie?.originCountry?.joinToString()
        } else {
            null
        }
    }

    private fun getProductionCompanies(): String? {
        return if (serie?.productionCompanies != null) {
            val list = mutableListOf<String>()
            serie?.productionCompanies?.forEach {
                list.add(it.name)
            }
            list.joinToString()
        } else {
            null
        }
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