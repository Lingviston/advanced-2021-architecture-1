package ru.gaket.themoviedb.data.auth.remote

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import ru.gaket.themoviedb.domain.auth.LogInError
import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.util.OperationResult
import ru.gaket.themoviedb.util.awaitTask
import ru.gaket.themoviedb.util.mapError
import ru.gaket.themoviedb.util.mapSuccess
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor() : AuthRemoteDataSource {

    private val firebaseAuth: FirebaseAuth get() = FirebaseAuth.getInstance()

    override val user: User?
        get() = firebaseAuth.currentUser?.let { firebaseUser ->
            val userId = firebaseAuth.currentUser?.uid?.let(User::Id)
            val userEmail = firebaseAuth.currentUser?.email?.let(User.Email::createIfValid)

            if ((userId != null) && (userEmail != null)) {
                User(userId, userEmail)
            } else {
                null
            }
        }

    override suspend fun logOut() =
        firebaseAuth.signOut()

    override suspend fun createNewUser(
        email: User.Email,
        password: User.Password,
    ): OperationResult<User.Id, RegisterError> =
        firebaseAuth.createUserWithEmailAndPassword(email.value, password.value)
            .getUserId()
            .mapError { error ->
                if (error is FirebaseAuthUserCollisionException) {
                    RegisterError.USER_WITH_SUCH_CREDENTIALS_EXISTS
                } else {
                    RegisterError.UNKNOWN
                }
            }

    override suspend fun logIn(
        email: User.Email,
        password: User.Password,
    ): OperationResult<User.Id, LogInError> =
        firebaseAuth.signInWithEmailAndPassword(email.value, password.value)
            .getUserId()
            .mapError { error ->
                if (error.isFirebaseInvalidUserCredentialsError()) {
                    LogInError.InvalidUserCredentials
                } else {
                    LogInError.Unknown
                }
            }
}

suspend fun Task<AuthResult>.getUserId(): OperationResult<User.Id, Throwable> =
    this.awaitTask()
        .mapSuccess { authResult -> authResult.toUserId() }

private fun AuthResult.toUserId(): User.Id =
    User.Id(requireNotNull(this.user).uid)

private fun Throwable.isFirebaseInvalidUserCredentialsError(): Boolean =
    when (this) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException,
        -> true
        else -> false
    }
