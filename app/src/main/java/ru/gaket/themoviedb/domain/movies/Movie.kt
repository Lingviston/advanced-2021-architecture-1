package ru.gaket.themoviedb.domain.movies

/**
 * Business class of Movies
 */
data class Movie(
	val id: Int,
	val name: String,
	val thumbnail: String?,
)