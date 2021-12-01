package com.vitor238.popcorn.ui.home.home.series

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.PopularTvSerie
import com.vitor238.popcorn.databinding.ItemMovieBinding
import com.vitor238.popcorn.utils.Constants

class PopularTvSeriesAdapter(private val clickListener: (popularTvSerie: PopularTvSerie) -> Unit) :
    ListAdapter<PopularTvSerie, PopularTvSeriesAdapter.ViewHolder>(PopularSeriesDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        private val imagePoster: ImageView = binding.imageMoviePoster
        private val textTitle: TextView = binding.textMovieTitle

        fun bind(
            popularTvSerie: PopularTvSerie,
            clickListener: (popularTvSerie: PopularTvSerie) -> Unit
        ) {
            textTitle.text = popularTvSerie.name
            Glide.with(imagePoster.context)
                .load(Constants.BASE_TMDB_IMG_URL_200 + popularTvSerie.posterPath)
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(imagePoster)

            binding.root.setOnClickListener {
                clickListener.invoke(popularTvSerie)
            }
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

    class PopularSeriesDiffUtils : DiffUtil.ItemCallback<PopularTvSerie>() {
        override fun areItemsTheSame(oldItem: PopularTvSerie, newItem: PopularTvSerie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PopularTvSerie, newItem: PopularTvSerie): Boolean {
            return oldItem == newItem
        }
    }
}