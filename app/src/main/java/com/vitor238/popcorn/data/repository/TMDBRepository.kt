package com.vitor238.popcorn.data.repository

import com.vitor238.popcorn.data.api.*
import com.vitor238.popcorn.data.model.*
import com.vitor238.popcorn.data.model.movie.Movie
import com.vitor238.popcorn.data.model.serie.Serie
import com.vitor238.popcorn.utils.RetrofitInitializer


class TMDBRepository {

    private val retrofit = RetrofitInitializer.getRetrofit()

    suspend fun getTrendingMoviesAndSeries(): List<Trend> {
        val trendsResult = retrofit.create(TrendsService::class.java)
            .getTrendingMoviesAndSeries()
        return trendsResult.results
    }

    suspend fun getPopularTVSeries(): List<PopularSerie> {
        val tvSeriesResult = retrofit.create(PopularSeriesService::class.java)
            .getPopularSeries()
        return tvSeriesResult.results
    }

    suspend fun getPopularMovies(): List<PopularMovie> {
        val popularMoviesResult = retrofit.create(PopularMoviesService::class.java)
            .getPopularMovies()
        return popularMoviesResult.results
    }

    suspend fun getSerieInfo(serieId: Int): Serie {
        return retrofit.create(SeriesService::class.java)
            .getSerieInfo(tvId = serieId)
    }

    suspend fun getMovieInfo(movieId: Int): Movie {
        return retrofit.create(MovieService::class.java)
            .getMovieInfo(movieId)
    }

    suspend fun getMovieRecommendations(movieId: Int): List<MovieRecommendation> {
        return retrofit.create(MovieRecommendationsService::class.java)
            .getMovieRecommendations(movieId = movieId).results
    }

    suspend fun getSerieRecommendations(serieId: Int): List<SerieRecommendation> {
        return retrofit.create(SerieRecommendationsService::class.java)
            .getSerieRecommendations(serieId = serieId).results
    }

    suspend fun getMoviesInTheaters(): List<NowPlaying> {
        return retrofit.create(NowPlayingService::class.java)
            .getMoviesOnTheaters().results
    }

    suspend fun searchMoviesOrSeries(query: String): List<MediaSearch> {
        return retrofit.create(SearchService::class.java).search(query = query)
            .results
    }
}