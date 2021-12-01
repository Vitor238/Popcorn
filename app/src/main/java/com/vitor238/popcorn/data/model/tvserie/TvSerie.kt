package com.vitor238.popcorn.data.model.tvserie

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vitor238.popcorn.data.model.Genres
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TvSerie(
    @Json(name = "original_name")
    val originalName: String?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "created_by")
    val createdBy: List<CreatedBy>?,
    @Json(name = "first_air_date")
    val firstAirDate: String?,
    val genres: List<Genres>?,
    val id: Int,
    @Json(name = "in_production")
    val inProduction: Boolean?,
    val languages: List<String>?,
    @Json(name = "last_air_date")
    val lastAirDate: String?,
    val name: String?,
    val networks: List<Networks>?,
    @Json(name = "number_of_episodes")
    val numberOfEpisodes: Int?,
    @Json(name = "number_of_seasons")
    val numberOfSeasons: Int?,
    @Json(name = "origin_country")
    val originCountry: List<String>?,
    val overview: String?,
    val popularity: Double?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "production_companies")
    val productionCompanies: List<ProductionCompanies>?,
) : Parcelable
