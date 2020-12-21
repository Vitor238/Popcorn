package com.vitor238.popcorn.ui.movieinfo

import android.util.Log
import androidx.lifecycle.*
import com.vitor238.popcorn.data.model.Favorite
import com.vitor238.popcorn.data.model.movie.Movie
import com.vitor238.popcorn.data.repository.FavoritesRepository
import com.vitor238.popcorn.data.repository.TMDBRepository
import com.vitor238.popcorn.utils.ApiStatus
import com.vitor238.popcorn.utils.FirestoreReferences
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val tmdbRepository = TMDBRepository()
    private val favoritesRepository = FavoritesRepository()

    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _movieInfo = MutableLiveData<Movie>()
    val movieInfo: LiveData<Movie>
        get() = _movieInfo

    val favorite: LiveData<Favorite> = favoritesRepository.favorite

    fun getMovieInfo(movieId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            val result = kotlin.runCatching { tmdbRepository.getMovieInfo(movieId) }
            result.onSuccess {
                _status.value = ApiStatus.DONE
                _movieInfo.value = it
            }.onFailure {
                _status.value = ApiStatus.ERROR
                Log.i("TESTE", "getMovieInfo: ${it.message}")
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