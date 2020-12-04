package com.vitor238.popcorn.data.repository

import com.vitor238.popcorn.data.api.*
import com.vitor238.popcorn.data.model.PopularMovie
import com.vitor238.popcorn.data.model.PopularSerie
import com.vitor238.popcorn.data.model.Trend
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

    companion object {
        private val TAG = TMDBRepository::class.simpleName
    }
}