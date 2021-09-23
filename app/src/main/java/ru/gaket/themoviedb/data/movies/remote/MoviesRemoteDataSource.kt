package ru.gaket.themoviedb.data.movies.remote

import ru.gaket.themoviedb.domain.movies.models.MovieId
import timber.log.Timber
import javax.inject.Inject

interface MoviesRemoteDataSource {

    suspend fun searchMovies(query: String, page: Int): List<SearchMovieDto>?
    suspend fun getMovieDetails(id: MovieId): DetailsMovieDto?
}

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val moviesApi: MoviesApi,
) : MoviesRemoteDataSource {

    /**
     * todo: use [ru.gaket.themoviedb.util.runOperationCatching] instead of runCatching everywhere, or vice versa
     */
    override suspend fun searchMovies(query: String, page: Int) = runCatching {
        moviesApi.searchMovie(query, page).searchMovies
    }.onFailure {
        Timber.e("Search movies from server error", it)
    }.getOrNull()

    override suspend fun getMovieDetails(id: MovieId) = runCatching {
        moviesApi.getMovieDetails(id)
    }.onFailure {
        Timber.e("Search movie from server error", it)
    }.getOrNull()
}
