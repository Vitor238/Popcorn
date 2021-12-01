package com.vitor238.popcorn.ui.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitor238.popcorn.data.Search
import com.vitor238.popcorn.data.model.MediaSearch
import com.vitor238.popcorn.data.repository.TMDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {

    private var _searchList = MutableLiveData<Search<List<MediaSearch>>>()
    val searchList: LiveData<Search<List<MediaSearch>>>
        get() = _searchList


    fun searchMovieOrSeries(query: String, includeAdult: Boolean) = viewModelScope.launch {
        _searchList.postValue(tmdbRepository.searchMoviesOrTvSeries(query, includeAdult))
    }
}