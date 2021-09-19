package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.gaket.themoviedb.data.genres.GenresRepository
import ru.gaket.themoviedb.data.genres.GenresRepositoryImpl
import ru.gaket.themoviedb.data.genres.local.GenresLocalDataSource
import ru.gaket.themoviedb.data.genres.local.GenresLocalDataSourceImpl
import ru.gaket.themoviedb.data.genres.remote.GenresRemoteDataSource
import ru.gaket.themoviedb.data.genres.remote.GenresRemoteDataSourceImpl
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.data.movies.MoviesRepositoryImpl
import ru.gaket.themoviedb.data.movies.local.MoviesLocalDataSource
import ru.gaket.themoviedb.data.movies.local.MoviesLocalDataSourceImpl
import ru.gaket.themoviedb.data.movies.remote.MoviesRemoteDataSource
import ru.gaket.themoviedb.data.movies.remote.MoviesRemoteDataSourceImpl
import ru.gaket.themoviedb.domain.movies.MoviesInteractor
import ru.gaket.themoviedb.domain.movies.MoviesInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModule {

    @Binds
    abstract fun bindMoviesRemoteDataSource(
        impl: MoviesRemoteDataSourceImpl,
    ): MoviesRemoteDataSource

    @Binds
    abstract fun bindMoviesLocalDataSource(
        impl: MoviesLocalDataSourceImpl,
    ): MoviesLocalDataSource

    @Binds
    abstract fun bindMoviesRepository(
        impl: MoviesRepositoryImpl,
    ): MoviesRepository

    @Binds
    abstract fun bindMoviesInteractor(
        impl: MoviesInteractorImpl,
    ): MoviesInteractor

    @Binds
    abstract fun bindGenresRemoteDataSource(
        impl: GenresRemoteDataSourceImpl,
    ): GenresRemoteDataSource

    @Binds
    abstract fun bindGenresLocalDataSource(
        impl: GenresLocalDataSourceImpl,
    ): GenresLocalDataSource

    @Binds
    abstract fun bindGenresRepository(
        impl: GenresRepositoryImpl,
    ): GenresRepository
}