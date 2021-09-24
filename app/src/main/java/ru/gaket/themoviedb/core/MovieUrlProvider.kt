package ru.gaket.themoviedb.core

data class MovieUrlProvider(
    val baseUrl: String,
    val apiKey: String,
    val baseImageUrl: String,
    val browseMovieBaseUrl: String,
)