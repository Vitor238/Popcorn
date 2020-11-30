package com.vitor238.popcorn.data.repository

import com.vitor238.popcorn.data.api.PopularSeriesService
import com.vitor238.popcorn.data.api.TrendsService
import com.vitor238.popcorn.data.model.PopularSerie
import com.vitor238.popcorn.data.model.Trend
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

    companion object {
        private val TAG = TMDBRepository::class.simpleName
    }
}