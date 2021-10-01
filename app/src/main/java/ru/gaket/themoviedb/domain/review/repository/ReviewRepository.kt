package ru.gaket.themoviedb.domain.review.repository

import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.Rating

interface ReviewRepository {

    suspend fun setMovieId(movieId: MovieId)
    suspend fun setWhatLike(whatLiked: String)
    suspend fun setWhatDidNotLike(whatDidNotLike: String)
    suspend fun setRating(rating: Rating)
    suspend fun clearState()

    @Throws(IllegalStateException::class)
    fun buildReview(): AddReviewRequest
}
