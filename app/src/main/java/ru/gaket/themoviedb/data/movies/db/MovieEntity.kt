package ru.gaket.themoviedb.data.movies.db

/**
 * DB class of Movies stored in room
 */
data class MovieEntity(
	val id: Int,
	val name: String,
	val thumbnail: String?
)