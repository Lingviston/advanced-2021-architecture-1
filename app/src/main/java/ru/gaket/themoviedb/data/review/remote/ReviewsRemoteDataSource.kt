package ru.gaket.themoviedb.data.review.remote

import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.AddReviewRequest
import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.domain.review.SomeoneReview
import ru.gaket.themoviedb.util.OperationResult

interface ReviewsRemoteDataSource {

    suspend fun getMyReviews(userId: User.Id): OperationResult<List<MyReview>, Throwable>

    suspend fun getMovieReviews(movieId: MovieId): OperationResult<List<SomeoneReview>, Throwable>

    suspend fun addReview(
        request: AddReviewRequest,
        authorId: User.Id,
        authorEmail: User.Email,
    ): OperationResult<MyReview, Throwable>
}