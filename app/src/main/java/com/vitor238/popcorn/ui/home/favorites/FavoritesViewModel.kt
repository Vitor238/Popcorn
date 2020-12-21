package com.vitor238.popcorn.ui.home.favorites

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.vitor238.popcorn.data.repository.FavoritesRepository

class FavoritesViewModel : ViewModel() {

    private val favoritesRepository = FavoritesRepository()
    val status = favoritesRepository.status
    val favorites = favoritesRepository.favoritesList

    fun getAllFavorites() {
        favoritesRepository.getAllFavorites()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun detachFavoritesListListener() {
        favoritesRepository.detachFavoritesListListener()
    }
}