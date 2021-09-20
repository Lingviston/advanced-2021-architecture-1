package ru.gaket.themoviedb.domain.auth

import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.util.VoidOperationResult
import ru.gaket.themoviedb.util.doOnSuccess
import javax.inject.Inject

class AuthInteractorImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val syncLocalStorageUseCase: SyncLocalStorageUseCase
) : AuthInteractor {

    override fun getCurrentUser(): User? =
        authRepository.currentUser

    override suspend fun auth(
        email: User.Email,
        password: User.Password
    ): VoidOperationResult<LogInError> =
        authRepository.auth(email, password)
            .doOnSuccess { syncLocalStorageUseCase.fill() }

    override suspend fun logOut() {
        authRepository.logOut()
        syncLocalStorageUseCase.clear()
    }
}