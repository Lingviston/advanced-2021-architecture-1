package ru.gaket.themoviedb.data.movies.local

/**
 * DB class of Movies stored in room
 */
data class SearchMovieEntity(
	val id: Int,
	val name: String,
	val thumbnail: String?,
)