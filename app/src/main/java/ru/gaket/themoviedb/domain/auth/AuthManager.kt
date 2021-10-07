package ru.gaket.themoviedb.domain.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    this.getCurrentUser().isAuthorized

fun AuthInteractor.observeIsAuthorized(): Flow<Boolean> =
    this.observeCurrentUser()
        .map { currentUser -> currentUser.isAuthorized }

private val User?.isAuthorized: Boolean
    get() = (this != null)