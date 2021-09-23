package ru.gaket.themoviedb.domain.auth

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.util.VoidOperationResult

interface AuthInteractor {

    fun getCurrentUser(): User?

    fun observeCurrentUser(): Flow<User?>

    suspend fun auth(
        email: User.Email,
        password: User.Password,
    ): VoidOperationResult<LogInError>

    suspend fun logOut()
}

fun AuthInteractor.isAuthorized(): Boolean =
    (this.getCurrentUser() != null)