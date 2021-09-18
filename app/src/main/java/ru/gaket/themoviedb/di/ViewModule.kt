package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.data.movies.MoviesRepositoryImpl
import ru.gaket.themoviedb.domain.movies.MoviesInteractor
import ru.gaket.themoviedb.domain.movies.MoviesInteractorImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModule {
	
	@Binds
	abstract fun bindMoviesRepository(
		impl: MoviesRepositoryImpl
	): MoviesRepository
	
	@Binds
	abstract fun bindMoviesInteractor(
		impl: MoviesInteractorImpl
	): MoviesInteractor
}