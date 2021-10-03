package ru.gaket.themoviedb.domain.review.models

import ru.gaket.themoviedb.domain.movies.models.MovieId

data class ReviewFormModel(
    val movieId: MovieId,
    val whatLiked: String?,
    val whatDidNotLike: String?,
    val rating: Rating?,
) {

    companion object {

        fun newEmptyModelInstance(movieId: MovieId) = ReviewFormModel(movieId, null, null, null)
    }
}
