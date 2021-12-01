package com.vitor238.popcorn.ui.movieinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.model.movie.Movie
import com.vitor238.popcorn.data.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel
@Inject
constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {

    private var _movieInfo = MutableLiveData<NetworkResult<Movie>>()
    val movieInfo: LiveData<NetworkResult<Movie>>
        get() = _movieInfo

    fun getMovieInfo(movieId: Int) = viewModelScope.launch {
        _movieInfo.postValue(tmdbRepository.getMovieInfo(movieId))
    }
}