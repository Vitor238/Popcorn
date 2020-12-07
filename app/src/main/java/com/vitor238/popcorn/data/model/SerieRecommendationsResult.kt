package com.vitor238.popcorn.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SerieRecommendationsResult(
    val page: Int,
    val results: List<SerieRecommendation>,
    @Json(name = "total_pages")
    val totalPages: Int,
)
