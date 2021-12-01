package com.vitor238.popcorn.ui.movieinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.model.MovieRecommendation
import com.vitor238.popcorn.data.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieRecommendationViewModel
@Inject
constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {

    private var _movieRecommendations = MutableLiveData<NetworkResult<List<MovieRecommendation>>>()
    val movieRecommendation: LiveData<NetworkResult<List<MovieRecommendation>>>
        get() = _movieRecommendations

    fun getRecommendations(movieId: Int) = viewModelScope.launch {
        _movieRecommendations.postValue(tmdbRepository.getMovieRecommendations(movieId))
    }
}