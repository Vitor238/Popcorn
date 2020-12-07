package com.vitor238.popcorn.ui.serieInfo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vitor238.popcorn.databinding.FragmentSerieRecommendationsBinding
import com.vitor238.popcorn.utils.ApiStatus

private const val SERIE_ID = "serieId"

class SerieRecommendationsFragment : Fragment() {
    private var serieId: Int? = null
    private var _binding: FragmentSerieRecommendationsBinding? = null
    private val binding: FragmentSerieRecommendationsBinding
        get() = _binding!!
    private lateinit var serieRecommendationAdapter: SerieRecommendationAdapter

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
        _binding = FragmentSerieRecommendationsBinding.inflate(layoutInflater, container, false)

        serieRecommendationAdapter = SerieRecommendationAdapter {
            val intent = Intent(requireActivity(), SerieInfoActivity::class.java)
            intent.putExtra("serieId", it.id)
            startActivity(intent)
        }

        binding.recyclerRecommendations.setHasFixedSize(true)
        binding.recyclerRecommendations.adapter = serieRecommendationAdapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val serieRecommendationViewModel = ViewModelProvider(this)
            .get(SerieRecommendationsViewModel::class.java)

        serieId?.let { id ->
            serieRecommendationViewModel.getRecommendadtions(id)

            serieRecommendationViewModel.serieRecommendation.observe(viewLifecycleOwner) { recommendations ->
                serieRecommendationAdapter.submitList(recommendations)
            }

            serieRecommendationViewModel.status.observe(viewLifecycleOwner) { status ->
                status?.let {
                    when (it) {
                        ApiStatus.LOADING -> binding.viewFlipper.displayedChild = 0
                        ApiStatus.DONE -> binding.viewFlipper.displayedChild = 1
                        ApiStatus.ERROR -> binding.viewFlipper.displayedChild = 2
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
            SerieRecommendationsFragment().apply {
                arguments = Bundle().apply {
                    putInt(SERIE_ID, serieId)
                }
            }
    }
}