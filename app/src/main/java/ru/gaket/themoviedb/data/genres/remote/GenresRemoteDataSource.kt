package ru.gaket.themoviedb.data.genres.remote

import timber.log.Timber
import javax.inject.Inject

interface GenresRemoteDataSource {
	suspend fun getGenres(): List<GenreDto>?
}

class GenresRemoteDataSourceImpl @Inject constructor(
	private val genresApi: GenresApi
) : GenresRemoteDataSource {

	override suspend fun getGenres() = runCatching {
		genresApi.getGenres()
	}.onFailure {
		Timber.e("Load Genres from server error", it)
	}.getOrNull()
}