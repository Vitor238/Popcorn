package com.vitor238.popcorn.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NowPlayingResult(
    val dates: Dates,
    val page: Int,
    val results: List<NowPlaying>
)
