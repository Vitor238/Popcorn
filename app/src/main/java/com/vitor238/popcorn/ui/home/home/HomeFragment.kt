package com.vitor238.popcorn.ui.home.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vitor238.popcorn.databinding.FragmentHomeBinding
import com.vitor238.popcorn.ui.viewmodel.TMDBViewModel
import com.vitor238.popcorn.utils.ApiStatus

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var trendsAdapter: TrendsAdapter
    private lateinit var tmdbViewModel: TMDBViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        trendsAdapter = TrendsAdapter()
        binding.recyclerTrends.setHasFixedSize(true)
        binding.recyclerTrends.adapter = trendsAdapter

        Log.i(TAG, "onCreateView: ")

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tmdbViewModel = ViewModelProvider(this).get(TMDBViewModel::class.java)
        tmdbViewModel.trends.observe(viewLifecycleOwner) { trends ->
            trendsAdapter.submitList(trends)
        }
        tmdbViewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                when (it) {
                    ApiStatus.LOADING -> binding.viewFlipperTrends.displayedChild = 0
                    ApiStatus.DONE -> binding.viewFlipperTrends.displayedChild = 1
                    ApiStatus.ERROR -> binding.viewFlipperTrends.displayedChild = 2
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