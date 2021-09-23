package ru.gaket.themoviedb.data.auth.local

import kotlinx.coroutines.flow.Flow
import ru.gaket.themoviedb.domain.auth.User

interface AuthLocalDataSource {

    var currentUser: User?

    fun observeCurrentUser(): Flow<User?>
}

fun AuthLocalDataSource.logOut() {
    this.currentUser = null
}