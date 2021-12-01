package com.vitor238.popcorn.ui.tvserieinfo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vitor238.popcorn.data.model.tvserie.TvSerie

class TvSeriesPagerAdapter(fragmentActivity: FragmentActivity, val tvSerie: TvSerie) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            TvSerieDetailsFragment.newInstance(tvSerie)
        } else {
            TvSerieRecommendationsFragment.newInstance(tvSerie.id)
        }
    }
}
