package com.vitor238.popcorn.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopularSerie(
    val popularity: Double?,
    val id: Int?,
    val name: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
)
