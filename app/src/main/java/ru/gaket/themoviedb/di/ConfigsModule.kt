package ru.gaket.themoviedb.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.BuildConfig
import ru.gaket.themoviedb.core.BuildConfigProvider
import ru.gaket.themoviedb.core.MovieUrlProvider

@Module
@InstallIn(SingletonComponent::class)
object ConfigsModule {

    @Provides
    fun provideBuildConfigProvider(): BuildConfigProvider =
        BuildConfigProvider(
            debug = BuildConfig.DEBUG,
            appId = BuildConfig.APPLICATION_ID,
            buildType = BuildConfig.BUILD_TYPE,
            flavor = BuildConfig.FLAVOR,
            versionCode = BuildConfig.VERSION_CODE,
            versionName = BuildConfig.VERSION_NAME
        )

    @Provides
    fun provideMovieUrlProvider(): MovieUrlProvider =
        MovieUrlProvider(
            baseUrl = BuildConfig.BASE_URL,
            apiKey = BuildConfig.THE_MOVIE_DB_API_KEY,
            //todo: to BuildConfig props?
            baseImageUrl = "https://image.tmdb.org/t/p/w300",
            browseMovieBaseUrl = "https://www.themoviedb.org/movie/"
        )
}