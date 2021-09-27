package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.BuildConfig
import com.vitor238.popcorn.data.model.SearchResult
import com.vitor238.popcorn.utils.LocaleUtils
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search/multi")
    suspend fun search(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = LocaleUtils.getLanguage(),
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false
    ): SearchResult
}