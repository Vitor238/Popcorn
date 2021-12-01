package com.vitor238.popcorn.ui.home.nowplaying

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.model.NowPlaying
import com.vitor238.popcorn.data.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel
@Inject
constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {

    private var _moviesInTheaters = MutableLiveData<NetworkResult<List<NowPlaying>>>()
    val moviesInTheaters: LiveData<NetworkResult<List<NowPlaying>>>
        get() = _moviesInTheaters

    fun getMoviesInTheaters() = viewModelScope.launch {
        _moviesInTheaters.postValue(tmdbRepository.getMoviesInTheaters())
    }

}