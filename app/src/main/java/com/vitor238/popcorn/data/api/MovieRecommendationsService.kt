package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.MovieRecommendationsResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieRecommendationsService {
    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): MovieRecommendationsResult
}