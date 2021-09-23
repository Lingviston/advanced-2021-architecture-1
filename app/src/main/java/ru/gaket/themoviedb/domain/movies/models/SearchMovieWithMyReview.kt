package ru.gaket.themoviedb.domain.movies.models

import ru.gaket.themoviedb.domain.review.MyReview

data class SearchMovieWithMyReview(
    val movie: SearchMovie,
    val myReview: MyReview?,
)