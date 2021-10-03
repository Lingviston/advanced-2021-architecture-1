package ru.gaket.themoviedb.domain.review.repository

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.domain.review.SomeoneReview
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel
import ru.gaket.themoviedb.util.OperationResult

interface ReviewRepository {

    val reviewState: Flow<ReviewFormModel>

    fun getMyReviews(movieId: MovieId): Flow<MyReview>
    suspend fun getSomeoneReviews(movieId: MovieId): OperationResult<List<SomeoneReview>, Throwable>

    suspend fun setMovieId(movieId: MovieId)
    suspend fun setWhatLike(whatLiked: String?)
    suspend fun setWhatDidNotLike(whatDidNotLike: String?)
    suspend fun setRating(rating: Rating?)

    suspend fun clearState()

    @Throws(IllegalStateException::class)
    fun buildReview(): AddReviewRequest
}
