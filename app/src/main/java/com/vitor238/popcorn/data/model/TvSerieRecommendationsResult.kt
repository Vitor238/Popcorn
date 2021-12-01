package com.vitor238.popcorn.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvSerieRecommendationsResult(
    val page: Int,
    val results: List<TvSerieRecommendation>,
    @Json(name = "total_pages")
    val totalPages: Int,
)
