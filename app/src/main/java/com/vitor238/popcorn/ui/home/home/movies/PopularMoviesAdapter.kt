package com.vitor238.popcorn.ui.home.home.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vitor238.popcorn.data.model.PopularMovie
import com.vitor238.popcorn.databinding.ItemMovieBinding
import com.vitor238.popcorn.utils.BaseUrls

class PopularMoviesAdapter :
    ListAdapter<PopularMovie, PopularMoviesAdapter.ViewHolder>(PopularMovieDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        private val imagePoster: ImageView = binding.imageMoviePoster
        private val textTitle: TextView = binding.textMovieTitle

        fun bind(popularMovie: PopularMovie) {
            textTitle.text = popularMovie.title
            Glide.with(imagePoster.context)
                .load(BaseUrls.BASE_TMDB_IMG_URL + popularMovie.posterPath)
                .into(imagePoster)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemMovieBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                return ViewHolder(binding)
            }
        }
    }

    class PopularMovieDiffUtils : DiffUtil.ItemCallback<PopularMovie>() {
        override fun areItemsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem == newItem
        }

    }
}