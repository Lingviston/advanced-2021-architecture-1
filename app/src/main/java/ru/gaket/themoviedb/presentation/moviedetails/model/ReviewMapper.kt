package ru.gaket.themoviedb.presentation.moviedetails.model

import ru.gaket.themoviedb.domain.review.models.SomeoneReview

fun SomeoneReview.toMovieDetailsReview() =
    MovieDetailsReview.SomeoneReview(author.value.substringBefore('@'), review)
