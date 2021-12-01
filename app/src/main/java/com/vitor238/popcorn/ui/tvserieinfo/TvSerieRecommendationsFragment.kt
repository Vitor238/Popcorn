package com.vitor238.popcorn.ui.tvserieinfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.databinding.FragmentTvSerieRecommendationsBinding
import dagger.hilt.android.AndroidEntryPoint

private const val SERIE_ID = "serieId"

@AndroidEntryPoint
class TvSerieRecommendationsFragment : Fragment() {
    private var serieId: Int? = null
    private var _binding: FragmentTvSerieRecommendationsBinding? = null
    private val binding: FragmentTvSerieRecommendationsBinding
        get() = _binding!!
    private lateinit var tvSerieRecommendationAdapter: TvSerieRecommendationAdapter
    private val serieRecommendationViewModel by viewModels<TvSerieRecommendationsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serieId = it.getInt(SERIE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvSerieRecommendationsBinding.inflate(layoutInflater, container, false)

        tvSerieRecommendationAdapter = TvSerieRecommendationAdapter {
            val intent = Intent(requireActivity(), TvSerieInfoActivity::class.java)
            intent.putExtra("serieId", it.id)
            startActivity(intent)
        }

        binding.recyclerRecommendations.setHasFixedSize(true)
        binding.recyclerRecommendations.adapter = tvSerieRecommendationAdapter
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {

        serieId?.let { id ->
            serieRecommendationViewModel.getRecommendations(id)
            binding.viewFlipper.displayedChild = 0

            serieRecommendationViewModel.serieRecommendation.observe(viewLifecycleOwner) { networkResult ->
                when (networkResult) {
                    is NetworkResult.Success -> {
                        binding.viewFlipper.displayedChild = 1
                        tvSerieRecommendationAdapter.submitList(networkResult.value)
                    }
                    is NetworkResult.Error -> {
                        binding.viewFlipper.displayedChild = 2
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
        fun newInstance(serieId: Int) =
            TvSerieRecommendationsFragment().apply {
                arguments = Bundle().apply {
                    putInt(SERIE_ID, serieId)
                }
            }
    }
}