package com.vitor238.popcorn.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopularMoviesResult(
    val page: Int,
    @Json(name = "total_pages")
    val totalPages: Int,
    val results: List<PopularMovie>,
    @Json(name = "total_results")
    val totalResults: Int
)
