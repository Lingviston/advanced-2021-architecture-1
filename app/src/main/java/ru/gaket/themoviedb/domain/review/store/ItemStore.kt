package ru.gaket.themoviedb.domain.review.store

import kotlinx.coroutines.flow.Flow

interface ItemStore<T> {

    val item: T?
    val itemChanges: Flow<T?>

    suspend fun setItem(item: T)

    //TODO [Vlad] Maybe move this method to extentions
    suspend fun updateItem(updateAction: (T) -> T)
    suspend fun reset()
}
