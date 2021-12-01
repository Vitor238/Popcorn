package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.PopularSeriesResult
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularTvSeriesService {
    @GET("tv/popular/")
    suspend fun getPopularTvSeries(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): PopularSeriesResult
}