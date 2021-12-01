package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.NowPlayingResult
import retrofit2.http.GET
import retrofit2.http.Query

interface NowPlayingService {
    @GET("movie/now_playing")
    suspend fun getMoviesOnTheaters(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("region") region: String
    ): NowPlayingResult
}