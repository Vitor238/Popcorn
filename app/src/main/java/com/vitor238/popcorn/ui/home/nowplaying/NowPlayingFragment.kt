package com.vitor238.popcorn.ui.home.nowplaying

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.databinding.FragmentNowPlayingBinding
import com.vitor238.popcorn.ui.movieinfo.MovieInfoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint()
class NowPlayingFragment : Fragment() {

    private var _binding: FragmentNowPlayingBinding? = null
    private val binding: FragmentNowPlayingBinding
        get() = _binding!!
    private val nowPlayingViewModel by viewModels<NowPlayingViewModel>()
    private lateinit var nowPlayingAdapter: NowPlayingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNowPlayingBinding.inflate(layoutInflater, container, false)

        nowPlayingAdapter = NowPlayingAdapter {
            val intent = MovieInfoActivity.getStartIntent(requireActivity(), it.id)
            startActivity(intent)
        }

        binding.recyclerNowPlaying.setHasFixedSize(true)
        binding.recyclerNowPlaying.adapter = nowPlayingAdapter

        observeViewModels()

        return binding.root
    }

    private fun observeViewModels() {
        nowPlayingViewModel.getMoviesInTheaters()
        binding.viewFlipper.displayedChild = 0

        nowPlayingViewModel.moviesInTheaters.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    binding.viewFlipper.displayedChild = 1
                    nowPlayingAdapter.submitList(result.value)
                }
                is NetworkResult.Error -> {
                    binding.viewFlipper.displayedChild = 2
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = NowPlayingFragment()
    }
}