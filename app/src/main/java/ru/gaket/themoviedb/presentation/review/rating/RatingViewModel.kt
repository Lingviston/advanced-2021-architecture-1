package ru.gaket.themoviedb.presentation.review.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.util.OperationResult.Error
import ru.gaket.themoviedb.util.OperationResult.Success
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val moviesRepository: MoviesRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val reviewEvent: LiveData<ReviewEvent>
        get() = _reviewEvent.asLiveData(viewModelScope.coroutineContext)
    private val _reviewEvent = MutableSharedFlow<ReviewEvent>()

    val reviewState: LiveData<ReviewState>
        get() = _reviewState
    private val _reviewState = MutableLiveData<ReviewState>()

    fun submit(ratingNumber: Int) {
        viewModelScope.launch {
            val rating = Rating.mapToRating(ratingNumber)
            if (rating == null) {
                _reviewEvent.emit(ReviewEvent.ZeroRatingError)
            } else {
                reviewRepository.setRating(rating)
                submitReview()
            }
        }
    }

    private suspend fun submitReview() {
        _reviewState.value = ReviewState.Loading

        val currentUser = authRepository.currentUser
        if (currentUser == null) {
            _reviewEvent.emit(ReviewEvent.UserNotSignedInError)
        } else {
            when (val review = reviewRepository.buildReview()) {
                is Success -> {
                    moviesRepository.addReview(review.result, currentUser.id, currentUser.email)
                    reviewRepository.clearState()
                    _reviewEvent.emit(ReviewEvent.Success)
                }
                is Error -> _reviewEvent.emit(ReviewEvent.UnknownError)
            }
        }

        _reviewState.value = ReviewState.Idle
    }

    sealed class ReviewState {
        object Idle : ReviewState()
        object Loading : ReviewState()
    }

    sealed class ReviewEvent {
        object ZeroRatingError : ReviewEvent()
        object UnknownError : ReviewEvent()
        object UserNotSignedInError : ReviewEvent()
        object Success : ReviewEvent()
    }
}