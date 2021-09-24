package ru.gaket.themoviedb.data.genres

import ru.gaket.themoviedb.data.genres.local.GenreEntity
import ru.gaket.themoviedb.data.genres.local.GenresLocalDataSource
import ru.gaket.themoviedb.data.genres.remote.GenreDto
import ru.gaket.themoviedb.data.genres.remote.GenresRemoteDataSource
import javax.inject.Inject

// todo: [Sergey] use domain model instead of data model
//todo: remove if useless with all related
interface GenresRepository {

    /**
     * @return List of [GenreEntity], null as error, or empty list.
     * No Throwable.
     */
    suspend fun getGenres(forceRefresh: Boolean): List<GenreEntity>?

    /**
     * @return List of [GenreEntity], null as error, or empty list.
     * No Throwable.
     */
    suspend fun getGenresByIds(forceRefresh: Boolean, ids: List<Int>): List<GenreEntity>?
}

class GenresRepositoryImpl @Inject constructor(
    private val remoteDataSource: GenresRemoteDataSource,
    private val localDataSource: GenresLocalDataSource,
) : GenresRepository {

    override suspend fun getGenres(forceRefresh: Boolean): List<GenreEntity>? {
        if (canLoadFromLocalCache(forceRefresh)) {
            return localDataSource.getGenres()
        }

        return syncFromServer()?.run { localDataSource.getGenres() }
    }

    // todo: [Sergey] use value class for Int
    override suspend fun getGenresByIds(forceRefresh: Boolean, ids: List<Int>): List<GenreEntity>? {
        if (canLoadFromLocalCache(forceRefresh)) {
            return localDataSource.getGenresByIds(ids)
        }

        return syncFromServer()?.run { localDataSource.getGenresByIds(ids) }
    }

    private suspend fun canLoadFromLocalCache(forceRefresh: Boolean): Boolean {
        return !forceRefresh && isLocalCacheIsNotEmpty()
    }

    private suspend fun syncFromServer(): List<GenreEntity>? =
        remoteDataSource.getGenres()
            ?.map(GenreDto::toEntity)
            ?.apply { localDataSource.insertAll(this) }

    private suspend fun isLocalCacheIsNotEmpty(): Boolean = localDataSource.hasData()
}
