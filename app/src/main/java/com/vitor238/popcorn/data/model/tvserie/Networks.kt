package com.vitor238.popcorn.data.model.tvserie

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Networks(
    val name: String,
    val id: Int,
    @Json(name = "logo_path")
    val logoPath: String
) : Parcelable
