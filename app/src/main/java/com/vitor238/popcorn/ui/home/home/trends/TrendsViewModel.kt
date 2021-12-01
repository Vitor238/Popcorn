package com.vitor238.popcorn.ui.home.home.trends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.model.Trend
import com.vitor238.popcorn.data.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendsViewModel
@Inject
constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {

    private val _trends = MutableLiveData<NetworkResult<List<Trend>>>()
    val trends: LiveData<NetworkResult<List<Trend>>>
        get() = _trends

    fun getTrends(mediaType: String, list: String) = viewModelScope.launch {
        _trends.postValue(tmdbRepository.getTrendingMoviesAndTvSeries(mediaType, list))
    }
}
