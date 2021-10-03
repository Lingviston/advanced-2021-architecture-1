package ru.gaket.themoviedb.domain.review

import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.auth.ReviewsSynchronizer
import javax.inject.Inject

class ReviewsSynchronizerImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val moviesRepository: MoviesRepository,
) : ReviewsSynchronizer {

    override suspend fun sync() =
        moviesRepository.getReviewsForUser(requireNotNull(authRepository.currentUser).id)

    override suspend fun unSync() =
        moviesRepository.deleteUserReviews()
}
