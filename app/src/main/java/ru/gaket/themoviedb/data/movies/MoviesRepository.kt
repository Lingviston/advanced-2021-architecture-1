package ru.gaket.themoviedb.data.movies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import ru.gaket.themoviedb.data.movies.local.MovieEntity
import ru.gaket.themoviedb.data.movies.remote.MoviesApi
import ru.gaket.themoviedb.di.BaseImageUrlQualifier
import timber.log.Timber
import javax.inject.Inject

interface MoviesRepository {

    suspend fun searchMovies(query: String, page: Int): List<MovieEntity>
}

/**
 * Repository providing data about [MovieEntity]
 */
class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    @BaseImageUrlQualifier private val baseImageUrl: String,
) : MoviesRepository {

    /**
     * Search [MovieEntity]s for the given [query] string
     */
    @FlowPreview
    override suspend fun searchMovies(query: String, page: Int): List<MovieEntity> {
        return flowOf(moviesApi.searchMovie(query, page))
            .flowOn(Dispatchers.IO)
            .onEach { Timber.d(it.movies.toString()) }
            .flatMapMerge { searchResponse -> searchResponse.movies.asFlow() }
            .map { movieDto -> movieDto.toEntity(baseImageUrl) }
            .toList()
    }
}