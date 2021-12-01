package com.vitor238.popcorn.ui.home.home.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.model.PopularMovie
import com.vitor238.popcorn.data.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel
@Inject
constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {

    private var _popularMovies = MutableLiveData<NetworkResult<List<PopularMovie>>>()
    val popularMovies: LiveData<NetworkResult<List<PopularMovie>>>
        get() = _popularMovies


    fun getPopularMovies() = viewModelScope.launch {
        _popularMovies.postValue(tmdbRepository.getPopularMovies())
    }
}