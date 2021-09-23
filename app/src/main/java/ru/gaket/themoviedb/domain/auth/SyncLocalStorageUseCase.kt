package ru.gaket.themoviedb.domain.auth

interface SyncLocalStorageUseCase {

    suspend fun sync()

    suspend fun unSync()
}