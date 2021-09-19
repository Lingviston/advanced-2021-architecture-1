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
import ru.gaket.themoviedb.data.movies.local.SearchMovieEntity
import ru.gaket.themoviedb.data.movies.remote.MoviesApi
import ru.gaket.themoviedb.di.BaseImageUrlQualifier
import timber.log.Timber
import javax.inject.Inject

interface MoviesRepository {

    suspend fun searchMovies(query: String, page: Int): List<SearchMovieEntity>
}

/**
 * Repository providing data about [SearchMovieEntity]
 */
class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    @BaseImageUrlQualifier private val baseImageUrl: String,
) : MoviesRepository {

    /**
     * Search [SearchMovieEntity]s for the given [query] string
     */
    @FlowPreview
    override suspend fun searchMovies(query: String, page: Int): List<SearchMovieEntity> {
        return flowOf(moviesApi.searchMovie(query, page))
            .flowOn(Dispatchers.IO)
            .onEach { Timber.d(it.searchMovies.toString()) }
            .flatMapMerge { searchResponse -> searchResponse.searchMovies.asFlow() }
            .map { movieDto -> movieDto.toEntity(baseImageUrl) }
            .toList()
    }
}