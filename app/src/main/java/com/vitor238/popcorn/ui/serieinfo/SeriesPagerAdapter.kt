package com.vitor238.popcorn.ui.serieinfo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vitor238.popcorn.data.model.serie.Serie

class SeriesPagerAdapter(fragmentActivity: FragmentActivity, val serie: Serie) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            SerieDetailsFragment.newInstance(serie)
        } else {
            SerieRecommendationsFragment.newInstance(serie.id)
        }
    }
}
