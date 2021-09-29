package ru.gaket.themoviedb.presentation.review.whatliked

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent
import javax.inject.Inject

@HiltViewModel
class WhatLikeViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    savedState: SavedStateHandle,
) : ViewModel() {

    private val _events = MutableSharedFlow<ReviewFieldEvent>()
    val events: LiveData<ReviewFieldEvent> get() = _events.asLiveData(viewModelScope.coroutineContext)

    init {
        val movieId: MovieId = savedState.get<MovieId>(ARG_MOVIE_ID)
            ?: error("You need to provide $ARG_MOVIE_ID")
        viewModelScope.launch {
            reviewRepository.clearState()
            reviewRepository.setMovieId(movieId)
        }
    }

    fun submitInfo(whatLike: String) {
        viewModelScope.launch {
            val fieldEvent = if (whatLike.isBlank()) {
                ReviewFieldEvent.EMPTY_FIELD
            } else {
                reviewRepository.setWhatLike(whatLike)
                ReviewFieldEvent.SUCCESS
            }
            _events.emit(fieldEvent)
        }
    }

    companion object {

        const val ARG_MOVIE_ID = "ARG_MOVIE_ID"
    }
}