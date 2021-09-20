package ru.gaket.themoviedb.data.review.remote

import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.MovieId
import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.domain.review.SomeoneElseReview
import ru.gaket.themoviedb.util.OperationResult

interface ReviewsRemoteDataSource {

    suspend fun getAllMyReviews(userId: User.Id): OperationResult<List<MyReview>, Throwable>

    suspend fun getAllReviewsFor(movieId: MovieId): OperationResult<List<SomeoneElseReview>, Throwable>

    suspend fun addReview(
        request: AddReviewRequest,
        authorId: User.Id,
        authorEmail: User.Email
    ): OperationResult<MyReview, Throwable>
}