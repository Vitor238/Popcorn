package com.vitor238.popcorn.ui.tvserieinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.model.TvSerieRecommendation
import com.vitor238.popcorn.data.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvSerieRecommendationsViewModel
@Inject
constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {

    private var _serieRecommendations =
        MutableLiveData<NetworkResult<List<TvSerieRecommendation>>>()
    val serieRecommendation: LiveData<NetworkResult<List<TvSerieRecommendation>>>
        get() = _serieRecommendations

    fun getRecommendations(tvSerieId: Int) = viewModelScope.launch {
        _serieRecommendations.postValue(tmdbRepository.getTvSerieRecommendations(tvSerieId))
    }
}
