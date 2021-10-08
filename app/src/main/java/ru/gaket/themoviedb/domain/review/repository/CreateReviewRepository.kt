package ru.gaket.themoviedb.domain.review.repository

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.domain.review.models.MovieWithCreateReviewState
import ru.gaket.themoviedb.domain.review.models.Rating
import ru.gaket.themoviedb.util.VoidResult

interface CreateReviewRepository {

    fun observeState(): Flow<MovieWithCreateReviewState>

    fun setWhatLike(whatLiked: String)
    fun setWhatDidNotLike(whatDidNotLike: String)
    fun setRating(rating: Rating)

    suspend fun submit(
        authorId: User.Id,
        authorEmail: User.Email,
    ): VoidResult<Throwable>
}