package ru.gaket.themoviedb.data.genres.remote

import javax.inject.Inject

interface GenresRemoteDataSource {
	suspend fun getGenres(): List<GenreDto>
}

class GenresRemoteDataSourceImpl @Inject constructor(
	private val genresApi: GenresApi
) : GenresRemoteDataSource {
	override suspend fun getGenres(): List<GenreDto> = genresApi.getGenres()
}