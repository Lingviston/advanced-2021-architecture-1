package ru.gaket.themoviedb.presentation.auth.viewmodel

import ru.gaket.themoviedb.domain.auth.LogInError

sealed class AuthState {

    object Empty : AuthState()

    object Authorizing : AuthState()

    object Authorized : AuthState()

    sealed class InputError : AuthState() {

        object Email : InputError()

        object Password : InputError()
    }

    data class AuthError(val logInError: LogInError) : AuthState()
}
