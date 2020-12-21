package com.vitor238.popcorn.ui.serieinfo

import android.util.Log
import androidx.lifecycle.*
import com.vitor238.popcorn.data.model.Favorite
import com.vitor238.popcorn.data.model.serie.Serie
import com.vitor238.popcorn.data.repository.FavoritesRepository
import com.vitor238.popcorn.data.repository.TMDBRepository
import com.vitor238.popcorn.utils.ApiStatus
import com.vitor238.popcorn.utils.FirestoreReferences
import kotlinx.coroutines.launch

class SerieViewModel : ViewModel() {
    private val tmdbRepository = TMDBRepository()
    private val favoritesRepository = FavoritesRepository()

    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _serieInfo = MutableLiveData<Serie>()
    val serieInfo: LiveData<Serie>
        get() = _serieInfo

    val favorite: LiveData<Favorite> = favoritesRepository.favorite

    fun getSerieInfo(serieId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            val result = kotlin.runCatching { tmdbRepository.getSerieInfo(serieId) }
            result.onSuccess {
                _status.value = ApiStatus.DONE
                Log.i("SerieViewModel", "getSerieInfo: ${it} ")
                _serieInfo.value = it
            }.onFailure {
                _status.value = ApiStatus.ERROR
                Log.i("SerieViewModel", "getSerieInfo: ${it.message} ")
            }
        }
    }

    fun checkFavorite(favorite: Favorite) {
        favoritesRepository.checkSaved(favorite)
    }

    fun saveFavorite(favorite: Favorite) {
        favorite.id = FirestoreReferences.favoritesRef.document().id
        favoritesRepository.saveFavorite(favorite)
    }

    fun removeFavorite(favorite: Favorite) {
        favoritesRepository.removeFavorite(favorite)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun detachFavotiteSavedListener() {
        favoritesRepository.detachFavoriteSavedListener()
    }
}