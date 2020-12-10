package com.vitor238.popcorn.ui.movieinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.model.MovieRecommendation
import com.vitor238.popcorn.data.repository.TMDBRepository
import com.vitor238.popcorn.utils.ApiStatus
import kotlinx.coroutines.launch

class MovieRecommendationViewModel : ViewModel() {
    val tmdbRepository = TMDBRepository()

    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _movieRecommendations = MutableLiveData<List<MovieRecommendation>>()
    val movieRecommendation: LiveData<List<MovieRecommendation>>
        get() = _movieRecommendations

    fun getRecommendadtions(movieId: Int) {
        viewModelScope.launch {
            val result = kotlin.runCatching { tmdbRepository.getMovieRecommendations(movieId) }
            _status.value = ApiStatus.LOADING
            result.onSuccess {
                _status.value = ApiStatus.DONE
                _movieRecommendations.value = it
            }.onFailure {
                _status.value = ApiStatus.ERROR
            }
        }
    }
}