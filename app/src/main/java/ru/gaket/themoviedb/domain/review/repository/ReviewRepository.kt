package ru.gaket.themoviedb.domain.review.repository

import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.util.OperationResult

interface ReviewRepository {

    suspend fun setMovieId(movieId: MovieId)
    suspend fun setWhatLike(whatLiked: String)
    suspend fun setWhatDidNotLike(whatDidNotLike: String)
    suspend fun setRating(rating: Rating)
    suspend fun clearState()
    fun buildReview(): OperationResult<AddReviewRequest, Throwable>
}