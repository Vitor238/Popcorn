package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.movie.Movie
import com.vitor238.popcorn.utils.ApiKeys
import com.vitor238.popcorn.utils.LocaleUtils
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/{movie_id}")
    suspend fun getMovieInfo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = ApiKeys.TMDB_API_KEY,
        @Query("language") language: String = LocaleUtils.getLanguage()
    ): Movie
}