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
import ru.gaket.themoviedb.presentation.review.ReviewWizard
import ru.gaket.themoviedb.util.OperationResult.Error
import ru.gaket.themoviedb.util.OperationResult.Success
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val reviewWizard: ReviewWizard,
    private val moviesRepository: MoviesRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val reviewEvent: LiveData<ReviewEvent>
        get() = _reviewEvent.asLiveData(viewModelScope.coroutineContext)
    private val _reviewEvent = MutableSharedFlow<ReviewEvent>()

    val reviewState: LiveData<ReviewState>
        get() = _reviewState
    private val _reviewState = MutableLiveData<ReviewState>()

    //TODO [Vlad] Improve code, maybe move to OperationResult
    fun submit(ratingNumber: Int) {
        val rating = Rating.mapToRating(ratingNumber)
        if (rating == null) {
            viewModelScope.launch { _reviewEvent.emit(ReviewEvent.ZeroRatingError) }
            return
        } else {
            reviewWizard.setRating(rating)
        }

        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading

            val currentUser = authRepository.currentUser
            val (userId, userEmail) = if (currentUser != null) {
                currentUser
            } else {
                _reviewEvent.emit(ReviewEvent.UserNotSignedInError)
                return@launch
            }

            when (val review = reviewWizard.buildReview()) {
                is Success -> {
                    moviesRepository.addReview(review.result, userId, userEmail)
                    reviewWizard.clearState()
                    _reviewEvent.emit(ReviewEvent.Success)
                }
                is Error -> _reviewEvent.emit(ReviewEvent.UnknownError)
            }
            _reviewState.value = ReviewState.Idle
        }
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