package ru.gaket.themoviedb.domain.auth

interface SyncLocalStorageUseCase {

    suspend fun fill()

    suspend fun clear()
}