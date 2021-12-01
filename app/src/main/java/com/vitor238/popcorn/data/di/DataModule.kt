package com.vitor238.popcorn.data.di

import com.vitor238.popcorn.BuildConfig
import com.vitor238.popcorn.data.api.*
import com.vitor238.popcorn.utils.Constants
import com.vitor238.popcorn.utils.Constants.BASE_TMDB_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun providesRetrofitInstance(
        converterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_TMDB_URL)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun providesMovieRecommendationsService(retrofit: Retrofit): MovieRecommendationsService =
        retrofit.create(MovieRecommendationsService::class.java)

    @Provides
    @Singleton
    fun providesMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    @Singleton
    fun providesNowPlayingService(retrofit: Retrofit): NowPlayingService =
        retrofit.create(NowPlayingService::class.java)

    @Provides
    @Singleton
    fun providesPopularMoviesService(retrofit: Retrofit): PopularMoviesService =
        retrofit.create(PopularMoviesService::class.java)

    @Provides
    @Singleton
    fun providesPopularSeriesService(retrofit: Retrofit): PopularTvSeriesService =
        retrofit.create(PopularTvSeriesService::class.java)

    @Provides
    @Singleton
    fun providesSearchService(retrofit: Retrofit): SearchService =
        retrofit.create(SearchService::class.java)


    @Provides
    @Singleton
    fun providesSerieRecommendationsService(retrofit: Retrofit): TvSeriesRecommendationsService =
        retrofit.create(TvSeriesRecommendationsService::class.java)

    @Provides
    @Singleton
    fun providesSeriesService(retrofit: Retrofit): TvSeriesService =
        retrofit.create(TvSeriesService::class.java)


    @Provides
    @Singleton
    fun providesTrendsService(retrofit: Retrofit): TrendsService =
        retrofit.create(TrendsService::class.java)

    @Provides
    @Singleton
    @Named(Constants.API_DI)
    fun providesApiKey(): String {
        return BuildConfig.TMDB_API_KEY
    }

    @Named(Constants.LANGUAGE_DI)
    @Provides
    fun provideLanguage(): String {
        return Locale.getDefault().toLanguageTag()
    }

    @Named(Constants.COUNTRY_DI)
    @Provides

    fun provideCountry(): String {
        return Locale.getDefault().country
    }
}