package ru.gaket.themoviedb.domain.review.models

import ru.gaket.themoviedb.domain.movies.models.MovieId

data class ReviewDraft(
    val movieId: MovieId,
    val liked: String,
    val disliked: String,
    val rating: Rating,
)
