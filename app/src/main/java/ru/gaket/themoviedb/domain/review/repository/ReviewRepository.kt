package ru.gaket.themoviedb.domain.review.repository

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel

interface ReviewRepository {

    val reviewState: Flow<ReviewFormModel>

    suspend fun setMovieId(movieId: MovieId)
    suspend fun setWhatLike(whatLiked: String)
    suspend fun setWhatDidNotLike(whatDidNotLike: String)
    suspend fun setRating(rating: Rating)
    suspend fun clearState()

    @Throws(IllegalStateException::class)
    fun buildReview(): AddReviewRequest
}
