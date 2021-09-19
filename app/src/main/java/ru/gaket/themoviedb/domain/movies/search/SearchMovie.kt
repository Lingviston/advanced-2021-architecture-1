package ru.gaket.themoviedb.domain.movies.search

/**
 * Business class of Movies
 */
data class SearchMovie(
	val id: Int,
	val name: String,
	val thumbnail: String?,
)