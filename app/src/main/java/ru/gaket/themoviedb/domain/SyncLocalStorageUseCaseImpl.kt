package ru.gaket.themoviedb.domain

import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.auth.SyncLocalStorageUseCase
import javax.inject.Inject

class SyncLocalStorageUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val moviesRepository: MoviesRepository,
) : SyncLocalStorageUseCase {

    override suspend fun sync() =
        moviesRepository.getReviewsForUser(requireNotNull(authRepository.currentUser).id)

    override suspend fun unSync() =
        moviesRepository.deleteUserReviews()
}
