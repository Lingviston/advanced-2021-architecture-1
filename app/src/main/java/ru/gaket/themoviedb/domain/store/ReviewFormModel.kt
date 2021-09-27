package ru.gaket.themoviedb.domain.store

import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.Rating

data class ReviewFormModel(
    val movieId: MovieId?,
    val whatLiked: String?,
    val whatDidNotLike: String?,
    val rating: Rating?,
)