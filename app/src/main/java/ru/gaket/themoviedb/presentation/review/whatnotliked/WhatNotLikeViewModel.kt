package ru.gaket.themoviedb.presentation.review.whatnotliked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent
import ru.gaket.themoviedb.domain.review.ReviewRepository
import javax.inject.Inject

@HiltViewModel
class WhatNotLikeViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    val events: LiveData<ReviewFieldEvent>
        get() = _events.asLiveData(viewModelScope.coroutineContext)
    private val _events = MutableSharedFlow<ReviewFieldEvent>()

    fun submitInfo(whatDidNotLike: String) {
        viewModelScope.launch {
            val fieldEvent = if (whatDidNotLike.isBlank()) {
                ReviewFieldEvent.EMPTY_FIELD
            } else {
                reviewRepository.setWhatDidNotLike(whatDidNotLike)
                ReviewFieldEvent.SUCCESS
            }
            _events.emit(fieldEvent)
        }
    }
}