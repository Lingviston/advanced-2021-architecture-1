package ru.gaket.themoviedb.presentation.review.whatnotliked

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent
import javax.inject.Inject

@HiltViewModel
class WhatNotLikeViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    private val _events = MutableStateFlow<ReviewFieldEvent?>(null)
    val events: LiveData<ReviewFieldEvent>
        get() = _events
            .filterNotNull()
            .asLiveData(viewModelScope.coroutineContext)

    fun submitInfo(whatDidNotLike: String) {
        viewModelScope.launch {
            val fieldEvent = if (whatDidNotLike.isBlank()) {
                ReviewFieldEvent.EMPTY_FIELD
            } else {
                reviewRepository.setWhatDidNotLike(whatDidNotLike)
                ReviewFieldEvent.SUCCESS
            }

            _events.value = fieldEvent
        }
    }
}
