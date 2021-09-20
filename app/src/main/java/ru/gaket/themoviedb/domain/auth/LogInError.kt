package ru.gaket.themoviedb.domain.auth

sealed class LogInError{

    object InvalidUserCredentials : LogInError()

    object Unknown : LogInError()
}