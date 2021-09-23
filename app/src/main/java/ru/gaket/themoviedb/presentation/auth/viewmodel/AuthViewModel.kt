package ru.gaket.themoviedb.presentation.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.gaket.themoviedb.domain.auth.AuthInteractor
import ru.gaket.themoviedb.domain.auth.User
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.domain.auth.LogInError
import ru.gaket.themoviedb.domain.auth.isAuthorized
import ru.gaket.themoviedb.util.OperationResult
import ru.gaket.themoviedb.util.VoidOperationResult
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(
        if (authInteractor.isAuthorized()) {
            AuthState.Authorized
        } else {
            AuthState.Empty
        }
    )

    val authState: LiveData<AuthState> get() = _authState

    fun auth(email: String, password: String) {
        val validatedEmail = User.Email.createIfValid(email)
        val validatedPassword = User.Password.createIfValid(password)

        when {
            (validatedEmail == null) -> {
                _authState.value = AuthState.InputError.Email
            }
            (validatedPassword == null) -> {
                _authState.value = AuthState.InputError.Password
            }
            else -> {
                executeAuthRequest(validatedEmail, validatedPassword)
            }
        }
    }

    private fun executeAuthRequest(email: User.Email, password: User.Password) {
        _authState.value = AuthState.Authorizing
        viewModelScope.launch {
            val result = authInteractor.auth(email, password)
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: VoidOperationResult<LogInError>) {
        _authState.value = when (result) {
            is OperationResult.Success -> AuthState.Authorized
            is OperationResult.Error -> AuthState.AuthError(result.result)
        }
    }
}