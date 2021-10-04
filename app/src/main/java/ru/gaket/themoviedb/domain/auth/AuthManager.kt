package ru.gaket.themoviedb.domain.auth

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.util.VoidResult

interface AuthInteractor {

    fun getCurrentUser(): User?

    fun observeCurrentUser(): Flow<User?>

    suspend fun auth(
        email: User.Email,
        password: User.Password,
    ): VoidResult<LogInError>

    suspend fun logOut()
}

fun AuthInteractor.isAuthorized(): Boolean =
    (this.getCurrentUser() != null)
