package ru.gaket.themoviedb.data.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.flow.MutableStateFlow
import ru.gaket.themoviedb.domain.auth.LogInError
import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.util.OperationResult
import ru.gaket.themoviedb.util.VoidOperationResult
import ru.gaket.themoviedb.util.awaitTask
import ru.gaket.themoviedb.util.mapError
import ru.gaket.themoviedb.util.mapSuccess
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val userFlow: MutableStateFlow<User?>

    private val firebaseAuth: FirebaseAuth get() = FirebaseAuth.getInstance()

    init {
        val userId = firebaseAuth.currentUser?.uid?.let(User::Id)
        val userEmail = firebaseAuth.currentUser?.email?.let(User.Email::createIfValid)
        val user: User? = if ((userId != null) && (userEmail != null)) {
            User(userId, userEmail)
        } else {
            null
        }

        userFlow = MutableStateFlow(user)
    }

    override val currentUser: User?
        get() = userFlow.value

    override suspend fun auth(email: User.Email, password: User.Password): VoidOperationResult<LogInError> =
        when (val registerResult = createNewUser(email, password)) {
            is OperationResult.Success -> registerResult
            is OperationResult.Error   -> handleRegisterError(registerResult, email, password)
        }

    override suspend fun logOut() {
        firebaseAuth.signOut()
        storeUserLocal(user = null)
    }

    private suspend fun createNewUser(
        email: User.Email,
        password: User.Password
    ): VoidOperationResult<RegisterError> =
        createNewUserAndReturnAuthResult(email, password)
            .mapSuccess { authResult -> saveSuccessAuthResult(authResult, email) }

    private suspend fun handleRegisterError(
        registerResult: OperationResult.Error<RegisterError>,
        email: User.Email,
        password: User.Password
    ): VoidOperationResult<LogInError> = when (registerResult.result) {
        RegisterError.UserWithSuchCredentialsExists -> logIn(email, password)
        RegisterError.Unknown                       -> OperationResult.Error(LogInError.Unknown)
    }

    private suspend fun logIn(
        email: User.Email,
        password: User.Password
    ): VoidOperationResult<LogInError> =
        logInAndReturnAuthResult(email, password)
            .mapSuccess { authResult -> saveSuccessAuthResult(authResult, email) }

    private suspend fun createNewUserAndReturnAuthResult(
        email: User.Email,
        password: User.Password
    ): OperationResult<AuthResult, RegisterError> =
        firebaseAuth.createUserWithEmailAndPassword(email.value, password.value)
            .awaitTask()
            .mapError { error ->
                if (error is FirebaseAuthUserCollisionException) {
                    RegisterError.UserWithSuchCredentialsExists
                } else {
                    RegisterError.Unknown
                }
            }

    private suspend fun logInAndReturnAuthResult(
        email: User.Email,
        password: User.Password
    ): OperationResult<AuthResult, LogInError> =
        firebaseAuth.signInWithEmailAndPassword(email.value, password.value)
            .awaitTask()
            .mapError { error ->
                if (error.isFirebaseInvalidUserCredentialsError()) {
                    LogInError.InvalidUserCredentials
                } else {
                    LogInError.Unknown
                }
            }

    private fun saveSuccessAuthResult(
        authResult: AuthResult,
        email: User.Email
    ) {
        val userUid = authResult.user!!.uid
        val user = User(id = User.Id(userUid), email = email)
        storeUserLocal(user)
    }

    private fun storeUserLocal(user: User?) {
        userFlow.value = user
    }
}

private fun Throwable.isFirebaseInvalidUserCredentialsError(): Boolean =
    when (this) {
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> true

        else                                -> false
    }

private sealed class RegisterError {

    object UserWithSuchCredentialsExists : RegisterError()

    object Unknown : RegisterError()
}