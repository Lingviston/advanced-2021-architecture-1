package ru.gaket.themoviedb.domain.movies.models

/**
 * Business class of searched Movies
 */
data class SearchMovie(
    val id: MovieId,
    val title: String,
    val thumbnail: String?,
)