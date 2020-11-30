package com.vitor238.popcorn.ui.home.home.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.model.PopularMovie
import com.vitor238.popcorn.data.repository.TMDBRepository
import com.vitor238.popcorn.utils.ApiStatus
import kotlinx.coroutines.launch

class PopularMoviesViewModel : ViewModel() {
    private val tmdbRepository = TMDBRepository()
    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status
    private var _popularMovies = MutableLiveData<List<PopularMovie>>()
    val popularMovies: LiveData<List<PopularMovie>>
        get() = _popularMovies


    private fun getPopularMovies() {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            val result = kotlin.runCatching { tmdbRepository.getPopularMovies() }
            result.onSuccess {
                _status.value = ApiStatus.DONE
                _popularMovies.value = it
            }.onFailure {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    init {
        getPopularMovies()
    }
}