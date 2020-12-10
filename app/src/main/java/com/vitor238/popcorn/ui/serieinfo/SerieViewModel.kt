package com.vitor238.popcorn.ui.serieinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.model.serie.Serie
import com.vitor238.popcorn.data.repository.TMDBRepository
import com.vitor238.popcorn.utils.ApiStatus
import kotlinx.coroutines.launch

class SerieViewModel : ViewModel() {
    val tmdbRepository = TMDBRepository()

    private var _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private var _serieInfo = MutableLiveData<Serie>()
    val serieInfo: LiveData<Serie>
        get() = _serieInfo

    fun getSerieInfo(serieId: Int) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            val result = kotlin.runCatching { tmdbRepository.getSerieInfo(serieId) }
            result.onSuccess {
                _status.value = ApiStatus.DONE
                Log.i("SerieViewModel", "getSerieInfo: ${it} ")
                _serieInfo.value = it
            }.onFailure {
                _status.value = ApiStatus.ERROR
                Log.i("SerieViewModel", "getSerieInfo: ${it.message} ")
            }
        }
    }
}