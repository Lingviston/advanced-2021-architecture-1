package ru.gaket.themoviedb.domain.review.models

import ru.gaket.themoviedb.domain.movies.models.Movie

sealed class MovieWithCreateReviewState {

    object NoMovie : MovieWithCreateReviewState()

    data class Data(
        val movie: Movie,
        val createState: CreateReviewState,
    ) : MovieWithCreateReviewState()
}