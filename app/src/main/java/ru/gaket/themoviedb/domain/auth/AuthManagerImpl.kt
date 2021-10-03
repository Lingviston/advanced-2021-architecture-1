package ru.gaket.themoviedb.domain.auth

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.util.VoidOperationResult
import ru.gaket.themoviedb.util.doOnSuccess
import javax.inject.Inject

class AuthManagerImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val reviewsSynchronizer: ReviewsSynchronizer,
) : AuthInteractor {

    override fun getCurrentUser(): User? =
        authRepository.currentUser

    override fun observeCurrentUser(): Flow<User?> =
        authRepository.observeCurrentUser()

    // TODO: decouple AuthManager from ReviewsSynchronizer
    //  subscribe synchronizer to AuthManager changes instead
    override suspend fun auth(
        email: User.Email,
        password: User.Password,
    ): VoidOperationResult<LogInError> =
        authRepository.auth(email, password)
            .doOnSuccess { reviewsSynchronizer.sync() }

    override suspend fun logOut() {
        authRepository.logOut()
        reviewsSynchronizer.unSync()
    }
}
