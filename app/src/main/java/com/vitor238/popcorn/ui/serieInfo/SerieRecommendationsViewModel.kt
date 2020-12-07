package com.vitor238.popcorn.ui.serieInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.model.SerieRecommendation
import com.vitor238.popcorn.data.repository.TMDBRepository
import com.vitor238.popcorn.utils.ApiStatus
import kotlinx.coroutines.launch

class SerieRecommendationsViewModel : ViewModel() {
    val tmdbRepository = TMDBRepository()

    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _serieRecommendations = MutableLiveData<List<SerieRecommendation>>()
    val serieRecommendation: LiveData<List<SerieRecommendation>>
        get() = _serieRecommendations

    fun getRecommendadtions(serieId: Int) {
        viewModelScope.launch {
            val result = kotlin.runCatching { tmdbRepository.getSerieRecommendations(serieId) }
            _status.value = ApiStatus.LOADING
            result.onSuccess {
                _status.value = ApiStatus.DONE
                _serieRecommendations.value = it
            }.onFailure {
                _status.value = ApiStatus.ERROR
            }
        }
    }
}
