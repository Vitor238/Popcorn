package com.vitor238.popcorn.data.api

import com.vitor238.popcorn.data.model.tvserie.TvSerie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvSeriesService {

    @GET("tv/{tv_id}")
    suspend fun getTvSerieInfo(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): TvSerie
}