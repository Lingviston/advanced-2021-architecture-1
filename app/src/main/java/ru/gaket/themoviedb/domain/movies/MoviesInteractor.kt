package ru.gaket.themoviedb.domain.movies

import ru.gaket.themoviedb.data.movies.MoviesRepository
import javax.inject.Inject

interface MoviesInteractor {

    suspend fun searchMovies(query: String, page: Int = 1): List<Movie>
}

class MoviesInteractorImpl @Inject constructor(
	private val moviesRepository: MoviesRepository,
) : MoviesInteractor {

    override suspend fun searchMovies(query: String, page: Int): List<Movie> =
        moviesRepository.searchMovies(query, page)
            .map { it.toModel() }
}