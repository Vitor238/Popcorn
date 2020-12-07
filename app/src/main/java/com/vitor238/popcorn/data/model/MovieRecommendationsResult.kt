package com.vitor238.popcorn.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieRecommendationsResult(
    val page: Int,
    val results: List<MovieRecommendation>,
    @Json(name = "total_pages")
    val total_pages: Int,
)
