package ru.gaket.themoviedb.data.movies

import ru.gaket.themoviedb.data.movies.local.MovieEntity
import ru.gaket.themoviedb.data.movies.local.MoviesLocalDataSource
import ru.gaket.themoviedb.data.movies.local.SearchMovieEntity
import ru.gaket.themoviedb.data.movies.remote.MoviesRemoteDataSource
import ru.gaket.themoviedb.di.BaseImageUrlQualifier
import javax.inject.Inject

interface MoviesRepository {

    suspend fun searchMovies(query: String, page: Int): List<SearchMovieEntity>?
    suspend fun getMovieDetails(id: Int): MovieEntity?
}

/**
 * Repository providing data about [MovieEntity], [SearchMovieEntity]
 */
class MoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val localDataSource: MoviesLocalDataSource,
    @BaseImageUrlQualifier private val baseImageUrl: String,
) : MoviesRepository {

    /**
     * Search [SearchMovieEntity]s for the given [query] string
     */
    override suspend fun searchMovies(query: String, page: Int): List<SearchMovieEntity>? {
        return remoteDataSource.searchMovies(query, page)
            ?.map { it.toEntity(baseImageUrl) }
            ?.apply { localDataSource.insertAll(this) }
    }

    /**
     * Get [MovieEntity] by movieId
     */
    override suspend fun getMovieDetails(id: Int): MovieEntity? {
        val cachedMovie = localDataSource.getById(id)
        return if (cachedMovie?.isUpdatedFromServer == true) {
            cachedMovie
        } else {
            remoteDataSource.getMovieDetails(id)
                ?.toEntity(
                    baseImageUrl,
                    hasReview = cachedMovie?.hasReview == true,
                    reviewId = cachedMovie?.reviewId ?: 0
                )
                ?.apply { localDataSource.update(this) }
                ?.run { localDataSource.getById(id) }
        }
    }
}