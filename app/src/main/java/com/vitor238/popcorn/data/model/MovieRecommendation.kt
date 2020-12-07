package com.vitor238.popcorn.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieRecommendation(
    val id: Int,
    @Json(name = "poster_path")
    val posterPath: String,
    val title: String
)
