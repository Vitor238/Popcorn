package com.vitor238.popcorn.ui.serieInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vitor238.popcorn.data.model.SerieRecommendation
import com.vitor238.popcorn.databinding.ItemMovieBinding
import com.vitor238.popcorn.utils.BaseUrls

class SerieRecommendationAdapter(private val clickListener: (serie: SerieRecommendation) -> Unit) :
    ListAdapter<SerieRecommendation, SerieRecommendationAdapter.ViewHolder>(RecommendationDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        private val imagePoster: ImageView = binding.imageMoviePoster
        private val textTitle: TextView = binding.textMovieTitle

        fun bind(serie: SerieRecommendation, clickListener: (serie: SerieRecommendation) -> Unit) {
            textTitle.text = serie.name
            Glide.with(imagePoster.context)
                .load(BaseUrls.BASE_TMDB_IMG_URL_200 + serie.posterPath)
                .into(imagePoster)

            binding.root.setOnClickListener {
                clickListener.invoke(serie)
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

    class RecommendationDiffUtils : DiffUtil.ItemCallback<SerieRecommendation>() {
        override fun areItemsTheSame(
            oldItem: SerieRecommendation,
            newItem: SerieRecommendation
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SerieRecommendation,
            newItem: SerieRecommendation
        ): Boolean {
            return oldItem == newItem
        }

    }

}