package com.vitor238.popcorn.ui.home.home.trends

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vitor238.popcorn.data.model.Trend
import com.vitor238.popcorn.databinding.ItemMovieBinding
import com.vitor238.popcorn.utils.BaseUrls.BASE_TMDB_IMG_URL

class TrendsAdapter :
    ListAdapter<Trend, TrendsAdapter.ViewHolder>(TrendsDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        private val imagePoster: ImageView = binding.imageMoviePoster
        private val textTitle: TextView = binding.textMovieTitle

        fun bind(trend: Trend) {
            textTitle.text = trend.name ?: trend.title
            Glide.with(imagePoster.context)
                .load(BASE_TMDB_IMG_URL + trend.posterPath)
                .into(imagePoster)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemMovieBinding
                    .inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                return ViewHolder(binding)
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
