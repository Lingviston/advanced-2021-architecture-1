package ru.gaket.themoviedb.domain.movies.models

import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.domain.review.SomeoneReview

data class MovieWithReviews(
    val movie: Movie,
    val someoneElseReviews: List<SomeoneReview>,
    val myReview: MyReview?,
)