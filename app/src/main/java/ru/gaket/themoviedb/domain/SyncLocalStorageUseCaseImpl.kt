package ru.gaket.themoviedb.domain

import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.auth.SyncLocalStorageUseCase
import ru.gaket.themoviedb.util.UnitTestable
import javax.inject.Inject

@UnitTestable
class SyncLocalStorageUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val moviesRepository: MoviesRepository,
) : SyncLocalStorageUseCase {

    override suspend fun sync() =
        moviesRepository.sync(requireNotNull(authRepository.currentUser).id)

    override suspend fun unSync() =
        moviesRepository.unSync()
}