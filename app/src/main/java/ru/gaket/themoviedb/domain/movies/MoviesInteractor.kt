package ru.gaket.themoviedb.domain.movies

import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.movies.models.SearchMovieWithMyReview
import javax.inject.Inject

interface MoviesInteractor {

    /**
     * @return List<SearchMovie>, null as error or empty list
     */
    suspend fun searchMovies(query: String, page: Int = 1): List<SearchMovieWithMyReview>?

    /**
     * @return List<SearchMovie>, null as error or empty list
     */
    suspend fun getMovieDetails(id: MovieId): Movie?
}

class MoviesInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : MoviesInteractor {

    override suspend fun searchMovies(query: String, page: Int): List<SearchMovieWithMyReview>? =
        moviesRepository.searchMoviesWithReviews(query, page)

    override suspend fun getMovieDetails(id: MovieId): Movie? =
        moviesRepository.getMovieDetails(id)
}