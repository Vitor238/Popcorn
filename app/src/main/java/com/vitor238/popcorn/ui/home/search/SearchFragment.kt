
package com.vitor238.popcorn.ui.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vitor238.popcorn.data.Search
import com.vitor238.popcorn.databinding.FragmentSearchBinding
import com.vitor238.popcorn.ui.home.MainActivity
import com.vitor238.popcorn.ui.movieinfo.MovieInfoActivity
import com.vitor238.popcorn.ui.tvserieinfo.TvSerieInfoActivity
import com.vitor238.popcorn.utils.Constants.MEDIA_TYPE_MOVIE
import com.vitor238.popcorn.utils.Constants.MEDIA_TYPE_TV
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint()
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!
    private lateinit var searchAdapter: SearchAdapter
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        searchAdapter = SearchAdapter {
            if (it.mediaType == MEDIA_TYPE_TV) {
                val intent = TvSerieInfoActivity.getStartIntent(requireActivity(), it.id)
                startActivity(intent)
            } else if (it.mediaType == MEDIA_TYPE_MOVIE) {
                val intent = MovieInfoActivity.getStartIntent(requireActivity(), it.id)
                startActivity(intent)
            }
        }

        binding.recyclerSearch.setHasFixedSize(true)
        binding.recyclerSearch.adapter = searchAdapter
        binding.viewFlipper.displayedChild = 0
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    binding.viewFlipper.displayedChild = 0
                } else {
                    searchViewModel.searchMovieOrSeries(query, false)
                }
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        searchViewModel.searchList.observe(viewLifecycleOwner) { search ->
            when (search) {
                is Search.NoResults -> {
                    binding.viewFlipper.displayedChild = 1
                    searchAdapter.submitList(null)
                }
                is Search.Success -> {
                    binding.viewFlipper.displayedChild = 2
                    searchAdapter.submitList(search.value)
                }
                is Search.Error -> {
                    binding.viewFlipper.displayedChild = 3
                }
            }
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