package ru.gaket.themoviedb.data.review.remote

import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.models.ReviewDraft
import ru.gaket.themoviedb.domain.review.models.MyReview
import ru.gaket.themoviedb.domain.review.models.SomeoneReview
import ru.gaket.themoviedb.util.OperationResult

interface ReviewsRemoteDataSource {

    suspend fun getMyReviews(userId: User.Id): OperationResult<List<MyReview>, Throwable>

    suspend fun getMovieReviews(movieId: MovieId): OperationResult<List<SomeoneReview>, Throwable>

    suspend fun addReview(
        draft: ReviewDraft,
        authorId: User.Id,
        authorEmail: User.Email,
    ): OperationResult<MyReview, Throwable>
}
