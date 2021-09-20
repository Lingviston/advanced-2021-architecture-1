package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.core.BuildConfigProvider
import ru.gaket.themoviedb.data.core.network.MoviesHttpClient
import ru.gaket.themoviedb.data.core.network.MoviesHttpClientImpl
import ru.gaket.themoviedb.data.genres.remote.GenresApi
import ru.gaket.themoviedb.data.movies.remote.MoviesApi
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    @Singleton
    fun bindMovieDbClient(
        impl: MoviesHttpClientImpl,
    ): MoviesHttpClient
}

@Module
@InstallIn(SingletonComponent::class)
object ApiWrapperModule {

    @Provides
    @Singleton
    fun provideMoviesApi(client: MoviesHttpClient): MoviesApi = client.moviesApi()

    @Provides
    @Singleton
    fun provideGenresApi(client: MoviesHttpClient): GenresApi = client.genresApi()
}

@Module
@InstallIn(SingletonComponent::class)
object BaseUrlWrapperModule {

    @Provides
    @Singleton
    @TheMovieDbApiKey
    fun provideApiKey(buildConfig: BuildConfigProvider) = buildConfig.apiKey

    @Provides
    @Singleton
    @BaseUrlQualifier
    fun provideBaseUrl(buildConfig: BuildConfigProvider) = buildConfig.baseUrl

    @Provides
    @Singleton
    @BaseImageUrlQualifier
    fun provideBaseImageUrl() = "https://image.tmdb.org/t/p/w300"

    @Provides
    @BrowseMovieBaseUrlQualifier
    fun provideBrowseMovieBaseUrl() = "https://www.themoviedb.org/movie/"
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TheMovieDbApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrlQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseImageUrlQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BrowseMovieBaseUrlQualifier