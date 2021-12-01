package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.TrendsResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrendsService {

    @GET("trending/{media_type}/{time_window}")
    suspend fun getTrendingMoviesAndTvSeries(
        @Path("media_type") mediaType: String,
        @Path("time_window") list: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): TrendsResult
}