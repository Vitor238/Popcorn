package com.vitor238.popcorn.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Genres(
    val id: Int,
    val name: String
) : Parcelable
