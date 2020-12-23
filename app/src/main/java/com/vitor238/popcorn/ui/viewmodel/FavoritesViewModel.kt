package com.vitor238.popcorn.ui.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.vitor238.popcorn.data.model.Favorite
import com.vitor238.popcorn.data.repository.FavoritesRepository
import com.vitor238.popcorn.utils.FirestoreReferences

class FavoritesViewModel(private val currentUserId: String) : ViewModel() {

    private val favoritesRepository = FavoritesRepository(currentUserId)
    val favorite: LiveData<Favorite> = favoritesRepository.favorite
    val status = favoritesRepository.status
    val favoritesList = favoritesRepository.favoritesList

    fun checkFavorite(favorite: Favorite) {
        favoritesRepository.checkSaved(favorite)
    }

    fun saveFavorite(favorite: Favorite) {
        favorite.id = FirestoreReferences.getFavoritesRef(currentUserId).document().id
        favoritesRepository.saveFavorite(favorite)
    }

    fun removeFavorite(favorite: Favorite) {
        favoritesRepository.removeFavorite(favorite)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun detachFavotiteSavedListener() {
        favoritesRepository.detachFavoriteSavedListener()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun detachFavoritesListListener() {
        favoritesRepository.detachFavoritesListListener()
    }

    fun getAllFavorites() {
        favoritesRepository.getAllFavorites()
    }


}