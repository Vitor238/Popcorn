package com.vitor238.popcorn.ui.movieinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vitor238.popcorn.data.model.MovieRecommendation
import com.vitor238.popcorn.databinding.ItemMovieBinding
import com.vitor238.popcorn.utils.Constants

class MovieRecommendationAdapter(private val clickListener: (movie: MovieRecommendation) -> Unit) :
    ListAdapter<MovieRecommendation, MovieRecommendationAdapter.ViewHolder>(RecommendationDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        private val imagePoster: ImageView = binding.imageMoviePoster
        private val textTitle: TextView = binding.textMovieTitle

        fun bind(movie: MovieRecommendation, clickListener: (movie: MovieRecommendation) -> Unit) {
            textTitle.text = movie.title
            Glide.with(imagePoster.context)
                .load(Constants.BASE_TMDB_IMG_URL_200 + movie.posterPath)
                .into(imagePoster)

            binding.root.setOnClickListener {
                clickListener.invoke(movie)
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

    class RecommendationDiffUtils : DiffUtil.ItemCallback<MovieRecommendation>() {
        override fun areItemsTheSame(
            oldItem: MovieRecommendation,
            newItem: MovieRecommendation
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieRecommendation,
            newItem: MovieRecommendation
        ): Boolean {
            return oldItem == newItem
        }

    }

}