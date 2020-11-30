package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.PopularMoviesRestult
import com.vitor238.popcorn.utils.ApiKeys
import com.vitor238.popcorn.utils.LocaleUtils
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularMoviesService {

    @GET("movie/popular/")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = ApiKeys.TMDB_API_KEY,
        @Query("language") language: String = LocaleUtils.getLanguage(),
        @Query("region") region: String = LocaleUtils.getCountry()
    ): PopularMoviesRestult
}