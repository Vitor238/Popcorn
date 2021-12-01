package com.vitor238.popcorn.data.api


import com.vitor238.popcorn.data.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search/multi")
    suspend fun search(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean
    ): SearchResult
}