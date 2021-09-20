package ru.gaket.themoviedb.data.movies.remote

import timber.log.Timber
import javax.inject.Inject

interface MoviesRemoteDataSource {

    suspend fun searchMovies(query: String, page: Int): List<SearchMovieDto>?
    suspend fun getMovieDetails(id: Int): DetailsMovieDto?
}

class MoviesRemoteDataSourceImpl @Inject constructor(
	private val moviesApi: MoviesApi,
) : MoviesRemoteDataSource {

    override suspend fun searchMovies(query: String, page: Int) = runCatching {
        moviesApi.searchMovie(query, page).searchMovies
    }.onFailure {
        Timber.e("Search movies from server error", it)
    }.getOrNull()

    override suspend fun getMovieDetails(id: Int) = runCatching {
        moviesApi.getMovieDetails(id)
    }.onFailure {
        Timber.e("Search movie from server error", it)
    }.getOrNull()
}
