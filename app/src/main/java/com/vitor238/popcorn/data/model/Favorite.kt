package com.vitor238.popcorn.data.model

data class Favorite(
    var id: String? = null,
    val mediaId: Int = 0,
    val mediaType: String = "",
    val title: String? = "",
    val posterPath: String? = ""
)