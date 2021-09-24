package ru.gaket.themoviedb.domain.movies

import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.movies.models.SearchMovieWithMyReview
import ru.gaket.themoviedb.util.OperationResult
import ru.gaket.themoviedb.util.UnitTestable
import javax.inject.Inject

interface MoviesInteractor {

    /**
     * @return List<SearchMovie>, null as error or empty list
     */
    suspend fun searchMovies(query: String, page: Int = 1): OperationResult<List<SearchMovieWithMyReview>, Throwable>

    /**
     * @return List<SearchMovie>, null as error or empty list
     */
    suspend fun getMovieDetails(id: MovieId): OperationResult<Movie, Throwable>
}

@UnitTestable
class MoviesInteractorImpl @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : MoviesInteractor {

    override suspend fun searchMovies(query: String, page: Int): OperationResult<List<SearchMovieWithMyReview>, Throwable> =
        moviesRepository.searchMoviesWithReviews(query, page)

    override suspend fun getMovieDetails(id: MovieId): OperationResult<Movie, Throwable> =
        moviesRepository.getMovieDetails(id)
}