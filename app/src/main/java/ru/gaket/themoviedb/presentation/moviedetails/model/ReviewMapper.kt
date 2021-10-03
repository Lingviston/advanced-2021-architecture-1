package ru.gaket.themoviedb.presentation.moviedetails.model

import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.domain.review.SomeoneReview

fun MyReview.toMovieDetailsReview() =
    MovieDetailsReview.MyReview(review)

fun SomeoneReview.toMovieDetailsReview() =
    MovieDetailsReview.SomeoneReview(author.value.substringBefore('@'), review)
