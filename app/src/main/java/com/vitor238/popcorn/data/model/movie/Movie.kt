package com.vitor238.popcorn.data.model.movie

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vitor238.popcorn.data.model.Genres
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    val genres: List<Genres>?,
    val homepage: String?,
    val id: Int,
    @Json(name = "original_title")
    val originalTitle: String?,
    val overview: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "production_companies")
    val productionCompanies: List<ProductionCompanies>?,
    @Json(name = "production_countries")
    val productionCountries: List<ProductionCountries>?,
    @Json(name = "release_date")
    val releaseDate: String?,
    val runtime: Int?,
    val tagline: String?,
    val title: String?,
) : Parcelable