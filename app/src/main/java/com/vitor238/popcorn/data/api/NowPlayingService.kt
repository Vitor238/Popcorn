package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.BuildConfig
import com.vitor238.popcorn.data.model.NowPlayingResult
import com.vitor238.popcorn.utils.LocaleUtils
import retrofit2.http.GET
import retrofit2.http.Query

interface NowPlayingService {
    @GET("movie/now_playing")
    suspend fun getMoviesOnTheaters(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = LocaleUtils.getLanguage(),
        @Query("region") region: String = LocaleUtils.getCountry()
    ): NowPlayingResult
}