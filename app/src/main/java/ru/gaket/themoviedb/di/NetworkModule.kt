package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.BuildConfig
import ru.gaket.themoviedb.data.core.network.MoviesHttpClient
import ru.gaket.themoviedb.data.core.network.MoviesHttpClientImpl
import ru.gaket.themoviedb.data.genres.remote.GenresApi
import ru.gaket.themoviedb.data.movies.remote.MoviesApi
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
	
	@Binds
	@Singleton
	abstract fun bindMovieDbClient(
		impl: MoviesHttpClientImpl
	): MoviesHttpClient
}

@Module
@InstallIn(SingletonComponent::class)
class ApiWrapperModule {

	@Provides
	fun provideMoviesApi(client: MoviesHttpClient): MoviesApi = client.moviesApi()
	
	@Provides
	fun provideGenresApi(client: MoviesHttpClient): GenresApi = client.genresApi()
}

@Module
@InstallIn(SingletonComponent::class)
class BaseUrlWrapperModule {

	@Provides
	@Singleton
	@BaseUrlQualifier
	fun provideBaseUrl() = BuildConfig.BASE_URL
	
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
annotation class BaseUrlQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseImageUrlQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BrowseMovieBaseUrlQualifier