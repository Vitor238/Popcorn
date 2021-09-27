package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.BuildConfig
import com.vitor238.popcorn.data.model.TrendsResult
import com.vitor238.popcorn.utils.LocaleUtils
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrendsService {

    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMoviesAndSeries(
        @Path("media_type") mediaType: String = "all",
        @Path("time_window") list: String = "week",
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = LocaleUtils.getLanguage()
    ): TrendsResult
}