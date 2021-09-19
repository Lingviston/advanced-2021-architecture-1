package ru.gaket.themoviedb.data.genres.remote

import retrofit2.http.GET

/**
 * Genres api of themoviedb.org
 */
interface GenresApi {
	
	@GET("/genre/movie/list")
	suspend fun getGenres(): List<GenreDto>
}