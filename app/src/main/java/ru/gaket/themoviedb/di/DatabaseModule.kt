package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.data.core.db.MoviesDbClient
import ru.gaket.themoviedb.data.core.db.MoviesDbClientImpl
import ru.gaket.themoviedb.data.genres.local.GenresDao
import ru.gaket.themoviedb.data.movies.local.MoviesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    @Singleton
    abstract fun bindMoviesDbClient(
		impl: MoviesDbClientImpl,
	): MoviesDbClient
}

@Module
@InstallIn(SingletonComponent::class)
class DaoWrapperModule {

    @Provides
    @Singleton
    fun provideGenresDao(client: MoviesDbClient): GenresDao = client.genresDao()

    @Provides
    @Singleton
    fun provideMoviesDao(client: MoviesDbClient): MoviesDao = client.moviesDao()
}