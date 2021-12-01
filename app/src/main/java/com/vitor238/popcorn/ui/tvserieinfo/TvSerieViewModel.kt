package com.vitor238.popcorn.ui.tvserieinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.model.tvserie.TvSerie
import com.vitor238.popcorn.data.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvSerieViewModel
@Inject
constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {

    private var _serieInfo = MutableLiveData<NetworkResult<TvSerie>>()
    val serieInfo: LiveData<NetworkResult<TvSerie>>
        get() = _serieInfo

    fun getSerieInfo(tvSerieId: Int) = viewModelScope.launch {
        _serieInfo.postValue(tmdbRepository.getTvSerieInfo(tvSerieId))
    }
}