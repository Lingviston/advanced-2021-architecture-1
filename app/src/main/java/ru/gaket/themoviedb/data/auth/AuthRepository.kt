package ru.gaket.themoviedb.data.auth

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.domain.auth.LogInError
import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.util.VoidResult

interface AuthRepository {

    val currentUser: User?

    fun observeCurrentUser(): Flow<User?>

    suspend fun auth(
        email: User.Email,
        password: User.Password,
    ): VoidResult<LogInError>

    suspend fun logOut()
}
