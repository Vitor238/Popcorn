package com.vitor238.popcorn.ui.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vitor238.popcorn.databinding.FragmentSearchBinding
import com.vitor238.popcorn.ui.home.MainActivity
import com.vitor238.popcorn.ui.movieinfo.MovieInfoActivity
import com.vitor238.popcorn.ui.serieinfo.SerieInfoActivity
import com.vitor238.popcorn.utils.MediaTypes
import com.vitor238.popcorn.utils.SearchStatus

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!
    private lateinit var searchAdapter: SearchAdapter
    private val searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        searchAdapter = SearchAdapter {
            if (it.mediaType == MediaTypes.TV) {
                val intent = SerieInfoActivity.getStartIntent(requireActivity(), it.id)
                startActivity(intent)
            } else if (it.mediaType == MediaTypes.MOVIE) {
                val intent = MovieInfoActivity.getStartIntent(requireActivity(), it.id)
                startActivity(intent)
            }
        }

        binding.recyclerSearch.setHasFixedSize(true)
        binding.recyclerSearch.adapter = searchAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.searchMovieOrSeries(it)
                }
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                when (it) {
                    SearchStatus.EMPTY -> binding.viewFlipper.displayedChild = 0
                    SearchStatus.DONE -> binding.viewFlipper.displayedChild = 1
                    SearchStatus.ERROR -> binding.viewFlipper.displayedChild = 2
                }
            }

        }

        searchViewModel.searchList.observe(viewLifecycleOwner) {
            searchAdapter.submitList(it)
        }

    }

    override fun onResume() {
        super.onResume()
        hideActivityToolbar(true)
    }

    override fun onStop() {
        super.onStop()
        hideActivityToolbar(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideActivityToolbar(hide: Boolean) {
        val mainActivity = activity as MainActivity
        if (hide) {
            mainActivity.supportActionBar?.hide()
        } else {
            mainActivity.supportActionBar?.show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}