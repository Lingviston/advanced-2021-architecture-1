package ru.gaket.themoviedb.data.auth.remote

import ru.gaket.themoviedb.domain.auth.LogInError
import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.util.Result

interface AuthRemoteDataSource {

    val user: User?

    suspend fun createNewUser(
        email: User.Email,
        password: User.Password,
    ): Result<User.Id, RegisterError>

    suspend fun logIn(
        email: User.Email,
        password: User.Password,
    ): Result<User.Id, LogInError>

    suspend fun logOut()
}
