package ru.gaket.themoviedb.domain.review

import ru.gaket.themoviedb.domain.auth.User

data class Review(
    val id: Review.Id,
    val liked: String,
    val disliked: String,
    val rating: Rating
) {
    @JvmInline
    value class Id(val value: String)
}

//todo
typealias MovieId = Int

data class MyReview(
    val movieId: MovieId,
    val review: Review
)

data class SomeoneElseReview(
    val author: User.Email,
    val review: Review
)