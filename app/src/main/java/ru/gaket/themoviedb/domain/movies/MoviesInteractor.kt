package ru.gaket.themoviedb.domain.movies

import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.movies.models.SearchMovie
import javax.inject.Inject

interface MoviesInteractor {

    /**
     * @return List<SearchMovie>, null as error or empty list
     */
    suspend fun searchMovies(query: String, page: Int = 1): List<SearchMovie>?

    /**
     * @return List<SearchMovie>, null as error or empty list
     */
    suspend fun getMovieDetails(id: Int): Movie?
}

class MoviesInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : MoviesInteractor {

    override suspend fun searchMovies(query: String, page: Int): List<SearchMovie>? =
        moviesRepository.searchMovies(query, page)
            ?.map { it.toModel() }

    override suspend fun getMovieDetails(id: Int): Movie? =
        moviesRepository.getMovieDetails(id)
            ?.toModel()
}