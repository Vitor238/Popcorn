package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.PopularSeriesResult
import com.vitor238.popcorn.utils.ApiKeys
import com.vitor238.popcorn.utils.LocaleUtils
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularSeriesService {
    @GET("tv/popular/")
    suspend fun getPopularSeries(
        @Query("api_key") apiKey: String = ApiKeys.TMDB_API_KEY,
        @Query("language") language: String = LocaleUtils.getLocale()
    ): PopularSeriesResult
}