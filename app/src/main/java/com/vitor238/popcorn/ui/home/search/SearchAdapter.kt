package com.vitor238.popcorn.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.MediaSearch
import com.vitor238.popcorn.databinding.ItemSearchBinding
import com.vitor238.popcorn.utils.BaseUrls

class SearchAdapter(private val clickListener: (mediaSearch: MediaSearch) -> Unit) :
    ListAdapter<MediaSearch, SearchAdapter.ViewHolder>(SearchDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }


    class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.textTitle
        private val description = binding.textOverview
        private val imagePoster = binding.imagePoster

        fun bind(mediaSearch: MediaSearch, clickListener: (mediaSearch: MediaSearch) -> Unit) {
            title.text = mediaSearch.title ?: mediaSearch.name
            description.text = mediaSearch.overview

            Glide.with(imagePoster.context)
                .load(BaseUrls.BASE_TMDB_IMG_URL_200 + mediaSearch.posterPath)
                .placeholder(R.drawable.ic_movie_placeholder)
                .into(imagePoster)

            binding.root.setOnClickListener {
                clickListener.invoke(mediaSearch)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding = ItemSearchBinding.inflate(
                    LayoutInflater.from(parent.context), parent,
                    false
                )
                return ViewHolder(binding)
            }
        }

    }

    class SearchDiffUtils : DiffUtil.ItemCallback<MediaSearch>() {
        override fun areItemsTheSame(oldItem: MediaSearch, newItem: MediaSearch): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MediaSearch, newItem: MediaSearch): Boolean {
            return oldItem == newItem
        }
    }
}