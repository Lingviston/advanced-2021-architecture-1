package ru.gaket.themoviedb.domain.review

import ru.gaket.themoviedb.domain.movies.models.MovieId

data class AddReviewRequest(
    val movieId: MovieId,
    val liked: String,
    val disliked: String,
    val rating: Rating
)