package com.vitor238.popcorn.ui.movieinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.model.movie.Movie
import com.vitor238.popcorn.data.repository.TMDBRepository
import com.vitor238.popcorn.utils.ApiStatus
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    val tmdbRepository = TMDBRepository()

    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _movieInfo = MutableLiveData<Movie>()
    val movieInfo: LiveData<Movie>
        get() = _movieInfo

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
}