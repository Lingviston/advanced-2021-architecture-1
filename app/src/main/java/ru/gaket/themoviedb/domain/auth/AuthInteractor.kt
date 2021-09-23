package ru.gaket.themoviedb.domain.auth

import ru.gaket.themoviedb.util.VoidOperationResult

interface AuthInteractor {

    fun getCurrentUser(): User?

    suspend fun auth(
        email: User.Email,
        password: User.Password,
    ): VoidOperationResult<LogInError>

    suspend fun logOut()
}

fun AuthInteractor.isAuthorized(): Boolean =
    (this.getCurrentUser() != null)