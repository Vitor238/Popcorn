package com.vitor238.popcorn.ui.home.nowplaying

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.model.NowPlaying
import com.vitor238.popcorn.data.repository.TMDBRepository
import com.vitor238.popcorn.utils.ApiStatus
import kotlinx.coroutines.launch

class NowPlayingViewModel : ViewModel() {
    private val tmdbRepository = TMDBRepository()
    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status
    private var _moviesInTheaters = MutableLiveData<List<NowPlaying>>()
    val moviesInTheaters: LiveData<List<NowPlaying>>
        get() = _moviesInTheaters


    private fun getMoviesInTheaters() {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            val result = kotlin.runCatching { tmdbRepository.getMoviesInTheaters() }
            result.onSuccess { movies ->
                val nullPosterLast = movies.sortedWith(compareBy(nullsLast()) { it.posterPath })
                _moviesInTheaters.value = nullPosterLast
                _status.value = ApiStatus.DONE
            }.onFailure {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    init {
        getMoviesInTheaters()
    }

    companion object {
        val TAG = NowPlayingViewModel::class.simpleName
    }
}