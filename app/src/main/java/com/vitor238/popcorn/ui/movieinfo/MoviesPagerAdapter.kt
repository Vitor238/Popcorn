package com.vitor238.popcorn.ui.movieinfo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vitor238.popcorn.data.model.movie.Movie

class MoviesPagerAdapter(fragmentActivity: FragmentActivity, val movie: Movie) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            MovieDetailsFragment.newInstance(movie)
        } else {
            MovieRecommendationsFragment.newInstance(movie.id)
        }
    }
}
