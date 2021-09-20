package ru.gaket.themoviedb.domain.review

data class AddReviewRequest(
    val movieId: MovieId,
    val liked: String,
    val disliked: String,
    val rating: Rating
)