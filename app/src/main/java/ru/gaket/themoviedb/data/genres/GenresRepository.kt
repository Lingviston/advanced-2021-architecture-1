package ru.gaket.themoviedb.data.genres

import ru.gaket.themoviedb.data.genres.local.GenreEntity
import ru.gaket.themoviedb.data.genres.local.GenresLocalDataSource
import ru.gaket.themoviedb.data.genres.remote.GenresRemoteDataSource
import javax.inject.Inject

interface GenresRepository {
	/**
	 * @return List<GenreEntity>, null as error or empty list
	 */
	suspend fun getGenres(forceRefresh: Boolean): List<GenreEntity>?
	
	/**
	 * @return List<GenreEntity>, null as error or empty list
	 */
	suspend fun getGenresByIds(forceRefresh: Boolean, ids: List<Int>): List<GenreEntity>?
}

class GenresRepositoryImpl @Inject constructor(
	private val remoteDataSource: GenresRemoteDataSource,
	private val localDataSource: GenresLocalDataSource
) : GenresRepository {

	override suspend fun getGenres(forceRefresh: Boolean): List<GenreEntity>? {
		if (!forceRefresh && hasData()) {
			return localDataSource.getGenres()
		}
		
		return syncFromServer()?.run { localDataSource.getGenres() }
	}
	
	override suspend fun getGenresByIds(forceRefresh: Boolean, ids: List<Int>): List<GenreEntity>? {
		if (!forceRefresh && hasData()) {
			return localDataSource.getGenresByIds(ids)
		}
		
		return syncFromServer()?.run { localDataSource.getGenresByIds(ids) }
	}
	
	private suspend fun syncFromServer(): List<GenreEntity>? =
		remoteDataSource.getGenres()
			?.map { it.toEntity() }
			?.apply { localDataSource.insertAll(this) }
	
	private suspend fun hasData(): Boolean = localDataSource.hasData()
}