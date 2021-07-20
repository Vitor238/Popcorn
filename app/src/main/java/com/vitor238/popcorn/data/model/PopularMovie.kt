package com.vitor238.popcorn.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopularMovie(
    val popularity: Double?,
    @Json(name = "poster_path")
    val posterPath: String?,
    val id: Int?,
    val title: String?
)
