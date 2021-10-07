package ru.gaket.themoviedb.domain.review.store

import kotlinx.coroutines.flow.Flow

interface ItemStore<T> {

    var item: T?

    fun observeItem(): Flow<T?>

    fun updateSafely(updateAction: (T) -> T)
}

fun <T> ItemStore<T>.reset() {
    this.item = null
}