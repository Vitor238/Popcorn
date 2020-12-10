package com.vitor238.popcorn.ui.home.nowplaying

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vitor238.popcorn.databinding.FragmentNowPlayingBinding
import com.vitor238.popcorn.ui.movieinfo.MovieInfoActivity
import com.vitor238.popcorn.utils.ApiStatus

class NowPlayingFragment : Fragment() {

    private var _binding: FragmentNowPlayingBinding? = null
    private val binding: FragmentNowPlayingBinding
        get() = _binding!!
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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val nowPlayingViewModel = ViewModelProvider(this).get(NowPlayingViewModel::class.java)
        nowPlayingViewModel.moviesInTheaters.observe(viewLifecycleOwner) {
            nowPlayingAdapter.submitList(it)
        }

        nowPlayingViewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                when (it) {
                    ApiStatus.LOADING -> binding.viewFlipper.displayedChild = 0
                    ApiStatus.DONE -> binding.viewFlipper.displayedChild = 1
                    ApiStatus.ERROR -> binding.viewFlipper.displayedChild = 2
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