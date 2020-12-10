package com.vitor238.popcorn.ui.home.nowplaying

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.NowPlaying
import com.vitor238.popcorn.databinding.ItemMovieBinding
import com.vitor238.popcorn.utils.BaseUrls

class NowPlayingAdapter(private val clickListener: (movie: NowPlaying) -> Unit) :
    ListAdapter<NowPlaying, NowPlayingAdapter.ViewHolder>(NowPlayingDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        private val imagePoster: ImageView = binding.imageMoviePoster
        private val textTitle: TextView = binding.textMovieTitle

        fun bind(movie: NowPlaying, clickListener: (movie: NowPlaying) -> Unit) {
            textTitle.text = movie.title
            Glide.with(imagePoster.context)
                .load(BaseUrls.BASE_TMDB_IMG_URL_200 + movie.posterPath)
                .placeholder(R.drawable.ic_movie_placeholder)
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

    class NowPlayingDiffUtils : DiffUtil.ItemCallback<NowPlaying>() {
        override fun areItemsTheSame(
            oldItem: NowPlaying,
            newItem: NowPlaying
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NowPlaying,
            newItem: NowPlaying
        ): Boolean {
            return oldItem == newItem
        }

    }

}