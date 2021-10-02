package ru.gaket.themoviedb.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.gaket.themoviedb.domain.review.store.ItemStore
import ru.gaket.themoviedb.util.synchronizedRead
import ru.gaket.themoviedb.util.synchronizedWrite
import java.util.concurrent.locks.ReentrantReadWriteLock

private const val BUFFER_CAPACITY = 16

class HeapItemStore<T : Any>(replay: Int) : ItemStore<T> {

    private var _item: T? = null
    private val _itemChanges = MutableSharedFlow<T?>(
        replay = replay,
        extraBufferCapacity = BUFFER_CAPACITY,
    )
    private val lock = ReentrantReadWriteLock()

    override val item: T?
        get() = synchronizedRead(lock) { _item }
    override val itemChanges: Flow<T?>
        get() = _itemChanges

    override suspend fun setItem(item: T) {
        synchronizedWrite(lock) {
            if (this._item == item) {
                return
            }

            this._item = item
        }

        _itemChanges.emit(item)
    }

    override suspend fun updateItem(updateAction: (T) -> T) {
        val updated: T

        synchronizedWrite(lock) {
            val current = this._item ?: return
            updated = updateAction.invoke(current)
            this._item = updated
        }

        _itemChanges.emit(updated)
    }

    override suspend fun reset() {
        synchronizedWrite(lock) {
            if (this._item == null) {
                return
            }

            _item = null
        }
        _itemChanges.emit(null)
    }
}
