package com.vitor238.popcorn.ui.home.home.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.model.PopularTvSerie
import com.vitor238.popcorn.data.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularTvSeriesViewModel
@Inject
constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {

    private var _popularTVSeries = MutableLiveData<NetworkResult<List<PopularTvSerie>>>()
    val popularTvSeries: LiveData<NetworkResult<List<PopularTvSerie>>>
        get() = _popularTVSeries

    fun getPopularTVSeries() = viewModelScope.launch {
        _popularTVSeries.postValue(tmdbRepository.getPopularTVSeries())
    }
}