package ru.gaket.themoviedb.domain.movies.models

/**
 * Business class of detailed Movies
 */
data class Movie(
    val id: Int = 0,
    val imdbId: String = "",
    val title: String = "",
    val overview: String = "",
    val allowedAge: String = "",
    val rating: Int = 0,
    val reviewsCounter: Int = 0,
    val popularity: Float = 0.00f,
    val releaseDate: String = "",
    val duration: Int = 0,
    val budget: Int = 0,
    val revenue: Int = 0,
    val status: String = "Released",
    val genres: String = "",
    val homepage: String = "",
    val thumbnail: String = "",
    val hasReview: Boolean = false,
    val reviewId: Int = 0,
    val isUpdatedFromServer: Boolean,
)