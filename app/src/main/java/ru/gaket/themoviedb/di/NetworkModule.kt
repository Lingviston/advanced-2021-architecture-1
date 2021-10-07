package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.data.core.network.MoviesHttpClient
import ru.gaket.themoviedb.data.core.network.MoviesHttpClientImpl
import ru.gaket.themoviedb.data.movies.remote.MoviesApi
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
    fun provideMoviesApi(client: MoviesHttpClient): MoviesApi = client.moviesApi
}
