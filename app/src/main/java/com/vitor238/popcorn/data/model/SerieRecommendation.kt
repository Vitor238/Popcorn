package com.vitor238.popcorn.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SerieRecommendation(
    val id: Int,
    val name: String,
    @Json(name = "original_name")
    val originalMame: String,
    @Json(name = "poster_path")
    val posterPath: String
)
