package ru.gaket.themoviedb.domain.movies.models

/**
 * Business class of detailed Movies
 */
data class Movie(
    val id: Long = 0, //
    val imdbId: String = "", //
    val title: String = "", //
    val overview: String = "", //
    val allowedAge: String = "", // "18+" or "13+"
    val rating: Int = 0, //
    val reviewsCounter: Int = 0, //
    val popularity: Float = 0.0f, //
    val releaseDate: String = "", // "1990-01-01"
    val duration: Int = 0, // в минутах
    val budget: Int = 0, //
    val revenue: Int = 0, // доход
    val status: String = "Released", //
    val genres: String = "", // "Documentary, Drama, Thriller"
    val homepage: String = "", // ?? всегда ""
    val thumbnail: String = "", // poster - полный url
    val hasReview: Boolean = false,
    val reviewId: Int = 0,
)

//todo:replace with value class
typealias MovieId = Long