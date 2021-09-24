package ru.gaket.themoviedb.domain.auth

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.util.UnitTestable
import ru.gaket.themoviedb.util.VoidOperationResult
import ru.gaket.themoviedb.util.doOnSuccess
import javax.inject.Inject

@UnitTestable
class AuthInteractorImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val syncLocalStorageUseCase: SyncLocalStorageUseCase,
) : AuthInteractor {

    override fun getCurrentUser(): User? =
        authRepository.currentUser

    override fun observeCurrentUser(): Flow<User?> =
        authRepository.observeCurrentUser()

    override suspend fun auth(
        email: User.Email,
        password: User.Password,
    ): VoidOperationResult<LogInError> =
        authRepository.auth(email, password)
            .doOnSuccess { syncLocalStorageUseCase.sync() }

    override suspend fun logOut() {
        authRepository.logOut()
        syncLocalStorageUseCase.unSync()
    }
}