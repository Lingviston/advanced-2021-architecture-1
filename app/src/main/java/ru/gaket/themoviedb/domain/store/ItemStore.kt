package ru.gaket.themoviedb.domain.store

import kotlinx.coroutines.flow.Flow

interface ItemStore<T> {

    val item: T
    val itemChanges: Flow<T>

    suspend fun setItem(item: T)
    suspend fun updateItem(updateAction: (T) -> T)
    suspend fun reset()
}