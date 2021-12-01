package com.vitor238.popcorn.data.repository

import android.util.Log
import com.vitor238.popcorn.data.NetworkResult
import com.vitor238.popcorn.data.Search
import com.vitor238.popcorn.data.api.*
import com.vitor238.popcorn.data.model.*
import com.vitor238.popcorn.data.model.movie.Movie
import com.vitor238.popcorn.data.model.tvserie.TvSerie
import com.vitor238.popcorn.utils.Constants
import javax.inject.Inject
import javax.inject.Named

class TMDBRepository
@Inject
constructor(
    @Named(Constants.API_DI)
    private val apiKey: String,
    @Named(Constants.LANGUAGE_DI)
    private val language: String,
    @Named(Constants.COUNTRY_DI)
    private val country: String,
    private val trendsService: TrendsService,
    private val popularTvSeriesService: PopularTvSeriesService,
    private val popularMoviesService: PopularMoviesService,
    private val tvSeriesService: TvSeriesService,
    private val movieService: MovieService,
    private val movieRecommendationsService: MovieRecommendationsService,
    private val tvSeriesRecommendationsService: TvSeriesRecommendationsService,
    private val nowPlayingService: NowPlayingService,
    private val searchService: SearchService
) {

    suspend fun getTrendingMoviesAndTvSeries(
        mediaType: String,
        list: String
    ): NetworkResult<List<Trend>> {
        Log.i("REPO", "getTrendingMoviesAndTvSeries: $language $country")
        return try {
            val trendsResult =
                trendsService.getTrendingMoviesAndTvSeries(mediaType, list, apiKey, language)
                    .results
            NetworkResult.Success(trendsResult)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    suspend fun getPopularTVSeries(): NetworkResult<List<PopularTvSerie>> {
        return try {
            val tvSeriesResult = popularTvSeriesService.getPopularTvSeries(apiKey, language).results
            NetworkResult.Success(tvSeriesResult)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    suspend fun getPopularMovies(): NetworkResult<List<PopularMovie>> {
        return try {
            val popularMoviesResult =
                popularMoviesService.getPopularMovies(apiKey, language, country)
                    .results
            NetworkResult.Success(popularMoviesResult)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    suspend fun getTvSerieInfo(tvSerieId: Int): NetworkResult<TvSerie> {
        return try {
            val result = tvSeriesService.getTvSerieInfo(tvSerieId, apiKey, language)
            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    suspend fun getMovieInfo(movieId: Int): NetworkResult<Movie> {
        return try {
            val result = movieService.getMovieInfo(movieId, apiKey, language)
            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    suspend fun getMovieRecommendations(movieId: Int): NetworkResult<List<MovieRecommendation>> {
        return try {
            val result = movieRecommendationsService
                .getMovieRecommendations(movieId, apiKey, language).results
            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    suspend fun getTvSerieRecommendations(tvSerieId: Int): NetworkResult<List<TvSerieRecommendation>> {
        return try {
            val result = tvSeriesRecommendationsService
                .getTvSerieRecommendations(tvSerieId, apiKey, language).results
            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    suspend fun getMoviesInTheaters(): NetworkResult<List<NowPlaying>> {
        return try {
            val result = nowPlayingService.getMoviesOnTheaters(apiKey, language, country).results
            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error(e.message)
        }
    }

    suspend fun searchMoviesOrTvSeries(
        query: String,
        includeAdult: Boolean
    ): Search<List<MediaSearch>> {
        return try {
            val result = searchService.search(apiKey, language, query, includeAdult).results
            val list = result.filterNot { it.mediaType == "person" }
            if (list.isEmpty()) {
                Search.NoResults
            } else {
                Search.Success(list)
            }
        } catch (e: Exception) {
            Search.Error(e.message)
        }
    }
}