package ru.gaket.themoviedb.presentation.review.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.domain.review.models.MovieWithCreateReviewState
import ru.gaket.themoviedb.domain.review.models.Rating
import ru.gaket.themoviedb.domain.review.repository.CreateReviewRepository
import ru.gaket.themoviedb.util.Result
import ru.gaket.themoviedb.util.exhaustive

class RatingViewModel @AssistedInject constructor(
    @Assisted private val createReviewRepository: CreateReviewRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _reviewEvent = MutableSharedFlow<Event>()
    val event: LiveData<Event>
        get() = _reviewEvent
            .asLiveData(viewModelScope.coroutineContext)

    private val _reviewState = MutableLiveData<State>()
    val state: LiveData<State> get() = _reviewState

    init {
        viewModelScope.launch {
            createReviewRepository.observeState()
                .filterIsInstance<MovieWithCreateReviewState.Data>()
                .map { state -> state.createState.form.rating }
                .filter { _reviewState.value == null }
                .collect { rating -> _reviewState.value = State.Idle(rating) }
        }
    }

    fun submit(ratingNumber: Int) {
        viewModelScope.launch {
            val rating = Rating.mapToRating(ratingNumber)
            if (rating == null) {
                _reviewEvent.emit(Event.ERROR_ZERO_RATING)
            } else {
                createReviewRepository.setRating(rating)
                submitReview()
            }
        }
    }

    private suspend fun submitReview() {
        val originalState = _reviewState.value!!
        if (originalState is State.Idle) {
            _reviewState.value = State.Loading

            val currentUser = authRepository.currentUser
            if (currentUser == null) {
                _reviewEvent.emit(Event.ERROR_USER_NOT_SIGNED)
            } else {
                val result = createReviewRepository.submit(
                    authorId = currentUser.id,
                    authorEmail = currentUser.email
                )
                when (result) {
                    is Result.Success -> Unit
                    is Result.Error -> _reviewEvent.emit(Event.ERROR_UNKNOWN)
                }.exhaustive
            }

            _reviewState.value = originalState
        }
    }

    sealed class State {
        data class Idle(val rating: Rating?) : State()
        object Loading : State()
    }

    enum class Event {
        ERROR_ZERO_RATING,
        ERROR_UNKNOWN,
        ERROR_USER_NOT_SIGNED
    }

    @AssistedFactory
    interface Factory {

        fun create(createReviewRepository: CreateReviewRepository): RatingViewModel
    }
}