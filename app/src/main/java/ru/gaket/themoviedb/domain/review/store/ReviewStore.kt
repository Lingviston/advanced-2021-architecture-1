package ru.gaket.themoviedb.domain.review.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel
import ru.gaket.themoviedb.domain.review.store.ItemStore
import javax.inject.Inject

private val emptyReviewModel get() = ReviewFormModel(null, null, null, null)

class ReviewStore @Inject constructor() : ItemStore<ReviewFormModel> {

    override val item: ReviewFormModel
        get() = _itemChanges.value

    override val itemChanges: Flow<ReviewFormModel>
        get() = _itemChanges
            .asSharedFlow()
    
    private val _itemChanges = MutableStateFlow(emptyReviewModel)

    override suspend fun setItem(item: ReviewFormModel) {
        _itemChanges.emit(item)
    }

    override suspend fun updateItem(updateAction: (ReviewFormModel) -> ReviewFormModel) {
        _itemChanges.emit(updateAction(item))
    }

    override suspend fun reset() {
        _itemChanges.emit(emptyReviewModel)
    }
}