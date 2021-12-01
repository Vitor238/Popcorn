package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.TvSerieRecommendationsResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvSeriesRecommendationsService {
    @GET("tv/{tv_id}/recommendations")
    suspend fun getTvSerieRecommendations(
        @Path("tv_id") serieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): TvSerieRecommendationsResult
}