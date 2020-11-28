package com.vitor238.popcorn.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Trend(
    @Json(name = "poster_path")
    val posterPath: String,
    @Json(name = "original_name")
    val originalName: String?,
    @Json(name = "original_title")
    val originalTitle: String?,
    val id: Int,
    @Json(name = "media_type")
    val mediaType: String
)
