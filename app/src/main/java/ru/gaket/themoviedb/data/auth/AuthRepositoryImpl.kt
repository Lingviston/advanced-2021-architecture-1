package ru.gaket.themoviedb.data.auth

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.data.auth.local.AuthLocalDataSource
import ru.gaket.themoviedb.data.auth.local.logOut
import ru.gaket.themoviedb.data.auth.remote.AuthRemoteDataSource
import ru.gaket.themoviedb.data.auth.remote.RegisterError
import ru.gaket.themoviedb.domain.auth.LogInError
import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.util.OperationResult
import ru.gaket.themoviedb.util.UnitTestable
import ru.gaket.themoviedb.util.VoidOperationResult
import ru.gaket.themoviedb.util.mapSuccess
import javax.inject.Inject

@UnitTestable
class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource,
) : AuthRepository {

    init {
        localDataSource.currentUser = remoteDataSource.user
    }

    override val currentUser: User?
        get() = localDataSource.currentUser

    override fun observeCurrentUser(): Flow<User?> =
        localDataSource.observeCurrentUser()

    override suspend fun auth(email: User.Email, password: User.Password): VoidOperationResult<LogInError> =
        when (val registerResult = createNewUser(email, password)) {
            is OperationResult.Success -> registerResult
            is OperationResult.Error -> handleRegisterError(registerResult, email, password)
        }

    override suspend fun logOut() {
        remoteDataSource.logOut()
        localDataSource.logOut()
    }

    private suspend fun createNewUser(
        email: User.Email,
        password: User.Password,
    ): VoidOperationResult<RegisterError> =
        remoteDataSource.createNewUser(email, password)
            .mapSuccess { userId -> saveSuccessAuthResult(userId, email) }

    private suspend fun handleRegisterError(
        registerResult: OperationResult.Error<RegisterError>,
        email: User.Email,
        password: User.Password,
    ): VoidOperationResult<LogInError> = when (registerResult.result) {
        RegisterError.USER_WITH_SUCH_CREDENTIALS_EXISTS -> logIn(email, password)
        RegisterError.UNKNOWN -> OperationResult.Error(LogInError.Unknown)
    }

    private suspend fun logIn(
        email: User.Email,
        password: User.Password,
    ): VoidOperationResult<LogInError> =
        remoteDataSource.logIn(email, password)
            .mapSuccess { userId -> saveSuccessAuthResult(userId, email) }

    private fun saveSuccessAuthResult(
        userId: User.Id,
        email: User.Email,
    ) {
        localDataSource.currentUser = User(userId, email)
    }
}