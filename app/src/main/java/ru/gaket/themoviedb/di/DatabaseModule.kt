package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.data.core.db.MoviesDbClient
import ru.gaket.themoviedb.data.core.db.MoviesDbClientImpl
import ru.gaket.themoviedb.data.genres.local.GenresDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
	
	@Binds
	@Singleton
	abstract fun bindMoviesDb(
		impl: MoviesDbClientImpl
	): MoviesDbClient
}

@Module
@InstallIn(SingletonComponent::class)
class DaoWrapperModule {
	
	@Provides
	fun provideGenresDao(client: MoviesDbClient): GenresDao = client.genresDao()
}