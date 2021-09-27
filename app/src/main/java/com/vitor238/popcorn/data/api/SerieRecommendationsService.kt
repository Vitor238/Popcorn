package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.BuildConfig
import com.vitor238.popcorn.data.model.SerieRecommendationsResult
import com.vitor238.popcorn.utils.LocaleUtils
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SerieRecommendationsService {
    @GET("tv/{tv_id}/recommendations")
    suspend fun getSerieRecommendations(
        @Path("tv_id") serieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = LocaleUtils.getLanguage()
    ): SerieRecommendationsResult
}