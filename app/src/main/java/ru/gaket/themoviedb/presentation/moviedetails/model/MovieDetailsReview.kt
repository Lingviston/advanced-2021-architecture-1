package ru.gaket.themoviedb.presentation.moviedetails.model

import ru.gaket.themoviedb.domain.review.Review

sealed class MovieDetailsReview {

    abstract val review: Review?

    data class MyReview(
        override val review: Review? = null,
    ) : MovieDetailsReview() {

        val isPosted: Boolean get() = review != null
    }

    data class SomeoneReview(
        val userName: String,
        override val review: Review,
    ) : MovieDetailsReview()
}


