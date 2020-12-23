package com.vitor238.popcorn.ui.home.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vitor238.popcorn.databinding.FragmentFavoritesBinding
import com.vitor238.popcorn.ui.movieinfo.MovieInfoActivity
import com.vitor238.popcorn.ui.serieinfo.SerieInfoActivity
import com.vitor238.popcorn.ui.viewmodel.FavoritesViewModel
import com.vitor238.popcorn.ui.viewmodel.FavoritesViewModelFactory
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModel
import com.vitor238.popcorn.ui.viewmodel.LoggedInViewModelFactory
import com.vitor238.popcorn.ui.welcome.WelcomeActivity
import com.vitor238.popcorn.utils.ApiStatus
import com.vitor238.popcorn.utils.MediaTypes

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding!!
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)

        favoritesAdapter = FavoritesAdapter {
            if (it.mediaType == MediaTypes.TV) {
                val intent = SerieInfoActivity.getStartIntent(requireContext(), it.mediaId)
                startActivity(intent)
            } else {
                val intent = MovieInfoActivity.getStartIntent(requireContext(), it.mediaId)
                startActivity(intent)
            }
        }
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = favoritesAdapter

        binding.imageLogin.setOnClickListener {
            val intent = Intent(requireActivity(), WelcomeActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val loggedInViewModelFactory = LoggedInViewModelFactory(requireActivity().application)
        val loggedInViewModel = ViewModelProvider(this, loggedInViewModelFactory)
            .get(LoggedInViewModel::class.java)

        loggedInViewModel.firebaseUserMutableLiveData.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser == null) {
                binding.viewFlipper.displayedChild = 0
            } else {
                initFavoritesViewModel(firebaseUser.uid)
            }
        }
    }

    private fun initFavoritesViewModel(userId: String) {
        val favoriteViewModelFactory = FavoritesViewModelFactory(userId)
        val favoritesViewModel = ViewModelProvider(this, favoriteViewModelFactory)
            .get(FavoritesViewModel::class.java)

        favoritesViewModel.getAllFavorites()

        favoritesViewModel.favoritesList.observe(viewLifecycleOwner) {
            favoritesAdapter.submitList(it)
        }
        favoritesViewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                when (it) {
                    ApiStatus.LOADING -> binding.viewFlipper.displayedChild = 1
                    ApiStatus.DONE -> binding.viewFlipper.displayedChild = 2
                    ApiStatus.ERROR -> binding.viewFlipper.displayedChild = 3
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}