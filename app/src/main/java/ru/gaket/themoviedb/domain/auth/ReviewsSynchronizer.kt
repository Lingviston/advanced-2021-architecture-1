package ru.gaket.themoviedb.domain.auth

interface ReviewsSynchronizer {

    suspend fun sync()

    suspend fun unSync()
}
