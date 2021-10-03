package ru.gaket.themoviedb.domain.review.models

import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.domain.movies.models.MovieId

data class Review(
    val id: Id,
    val liked: String,
    val disliked: String,
    val rating: Rating,
) {

    @JvmInline
    value class Id(val value: String)
}

data class MyReview(
    val movieId: MovieId,
    val review: Review,
)

data class SomeoneReview(
    val author: User.Email,
    val review: Review,
)
