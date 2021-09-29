package ru.gaket.themoviedb.data.review.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel
import ru.gaket.themoviedb.domain.review.store.ItemStore
import javax.inject.Inject

class ReviewStore @Inject constructor() : ItemStore<ReviewFormModel> {

    private val _itemChanges = MutableStateFlow(emptyReviewModel)

    override val item: ReviewFormModel = _itemChanges.value

    override val itemChanges: Flow<ReviewFormModel> = _itemChanges.asSharedFlow()

    override suspend fun setItem(item: ReviewFormModel) {
        _itemChanges.emit(item)
    }

    override suspend fun updateItem(updateAction: (ReviewFormModel) -> ReviewFormModel) {
        _itemChanges.emit(updateAction(item))
    }

    override suspend fun reset() {
        _itemChanges.emit(emptyReviewModel)
    }

    companion object {

        private val emptyReviewModel get() = ReviewFormModel(null, null, null, null)
    }
}