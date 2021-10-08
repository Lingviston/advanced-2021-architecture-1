package ru.gaket.themoviedb.presentation.review.whatnotliked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.domain.review.models.MovieWithCreateReviewState
import ru.gaket.themoviedb.domain.review.repository.CreateReviewRepository
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent

class WhatNotLikeViewModel @AssistedInject constructor(
    @Assisted private val createReviewRepository: CreateReviewRepository,
) : ViewModel() {

    private val _events = MutableSharedFlow<ReviewFieldEvent>()
    val events: LiveData<ReviewFieldEvent>
        get() = _events
            .asLiveData(viewModelScope.coroutineContext)

    val initialValue: LiveData<String> = createReviewRepository.observeState()
        .filterIsInstance<MovieWithCreateReviewState.Data>()
        .map { state -> state.createState.form.whatDidNotLike }
        .filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)

    fun submitInfo(whatDidNotLike: String) {
        viewModelScope.launch {
            if (whatDidNotLike.isBlank()) {
                _events.emit(ReviewFieldEvent.EMPTY_FIELD)
            } else {
                createReviewRepository.setWhatDidNotLike(whatDidNotLike)
            }
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(createReviewRepository: CreateReviewRepository): WhatNotLikeViewModel
    }
}