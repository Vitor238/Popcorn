package com.vitor238.popcorn.ui.home.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.Favorite
import com.vitor238.popcorn.databinding.ItemMovieBinding
import com.vitor238.popcorn.ui.home.home.trends.TrendsAdapter
import com.vitor238.popcorn.utils.Constants

class FavoritesAdapter(private val clickListener: (favorite: Favorite) -> Unit) :
    ListAdapter<Favorite, FavoritesAdapter.ViewHolder>(FavoritesDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val imagePoster: ImageView = binding.imageMoviePoster
        private val textTitle: TextView = binding.textMovieTitle

        fun bind(favorite: Favorite, clickListener: (favorite: Favorite) -> Unit) {

            textTitle.text = favorite.title

            Glide.with(imagePoster.context)
                .load(Constants.BASE_TMDB_IMG_URL_200 + favorite.posterPath)
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(imagePoster)

            binding.root.setOnClickListener {
                clickListener.invoke(favorite)
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

    class FavoritesDiffUtils : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        val TAG = TrendsAdapter::class.simpleName
    }
}