package ru.gaket.themoviedb.presentation.review

import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel

sealed class MovieWithReviewViewState {

    object NoMovie : MovieWithReviewViewState()

    data class MovieWithReview(
        val movie: Movie,
        val review: ReviewFormModel,
    ) : MovieWithReviewViewState()
}
