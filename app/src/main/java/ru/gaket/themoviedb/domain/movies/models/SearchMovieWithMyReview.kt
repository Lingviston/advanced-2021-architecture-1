package ru.gaket.themoviedb.domain.movies.models

import ru.gaket.themoviedb.domain.review.models.MyReview

data class SearchMovieWithMyReview(
    val movie: SearchMovie,
    val myReview: MyReview?,
)
