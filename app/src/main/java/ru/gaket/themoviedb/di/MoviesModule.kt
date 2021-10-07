package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.data.movies.MoviesRepositoryImpl
import ru.gaket.themoviedb.data.movies.local.MoviesLocalDataSource
import ru.gaket.themoviedb.data.movies.local.MoviesLocalDataSourceImpl
import ru.gaket.themoviedb.data.movies.remote.MoviesRemoteDataSource
import ru.gaket.themoviedb.data.movies.remote.MoviesRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
interface MoviesModule {

    @Binds
    fun bindMoviesRemoteDataSource(
        impl: MoviesRemoteDataSourceImpl,
    ): MoviesRemoteDataSource

    @Binds
    fun bindMoviesLocalDataSource(
        impl: MoviesLocalDataSourceImpl,
    ): MoviesLocalDataSource

    @Binds
    fun bindMoviesRepository(
        impl: MoviesRepositoryImpl,
    ): MoviesRepository
}
