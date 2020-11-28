package com.vitor238.popcorn.ui.home.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.Trend
import com.vitor238.popcorn.utils.BaseUrls.BASE_TMDB_IMG_URL
import kotlinx.android.synthetic.main.item_movie.view.*

class TrendsAdapter :
    ListAdapter<Trend, TrendsAdapter.ViewHolder>(TrendsDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        private val imagePoster: ImageView = item.image_movie_poster
        private val textTitle: TextView = item.text_movie_title

        fun bind(trend: Trend) {
            textTitle.text = trend.originalName ?: trend.originalTitle
            Glide.with(imagePoster.context)
                .load(BASE_TMDB_IMG_URL + trend.posterPath)
                .into(imagePoster)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_movie, parent, false)
                return ViewHolder(view)
            }
        }
    }

    class TrendsDiffUtils : DiffUtil.ItemCallback<Trend>() {
        override fun areItemsTheSame(oldItem: Trend, newItem: Trend): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Trend, newItem: Trend): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        val TAG = TrendsAdapter::class.simpleName
    }
}
