package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.PopularMoviesResult
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularMoviesService {

    @GET("movie/popular/")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("region") region: String
    ): PopularMoviesResult
}