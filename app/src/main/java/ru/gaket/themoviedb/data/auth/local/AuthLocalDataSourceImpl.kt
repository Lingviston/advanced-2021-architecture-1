package ru.gaket.themoviedb.data.auth.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.gaket.themoviedb.domain.auth.User
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor() : AuthLocalDataSource {

    private val userFlow: MutableStateFlow<User?> = MutableStateFlow(null)

    override var currentUser: User?
        get() = userFlow.value
        set(newValue) {
            userFlow.value = newValue
        }

    override fun observeCurrentUser(): Flow<User?> = userFlow
}