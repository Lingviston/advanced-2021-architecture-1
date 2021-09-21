package ru.gaket.themoviedb.presentation.review.rating

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.presentation.review.ReviewWizard
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val reviewWizard: ReviewWizard,
    private val moviesRepository: MoviesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val reviewEvent: LiveData<ReviewEvent>
        get() = _reviewEvent.asLiveData(viewModelScope.coroutineContext)
    private val _reviewEvent = MutableSharedFlow<ReviewEvent>()

    val reviewState: LiveData<ReviewState>
        get() = _reviewState
    private val _reviewState = MutableLiveData<ReviewState>()

    fun submit(ratingNumber: Int) {
        val rating = Rating.mapToRating(ratingNumber)
        if (rating == null) {
            viewModelScope.launch { _reviewEvent.emit(ReviewEvent.ZeroRatingError) }
            return
        } else {
            reviewWizard.setRating(rating)
        }

        viewModelScope.launch {
            try {
                _reviewState.value = ReviewState.Loading
                val (userId, userEmail) = authRepository.currentUser
                    ?: error("User is not signed in")
                moviesRepository.addReview(
                    reviewWizard.buildReview(),
                    userId,
                    userEmail
                )
                reviewWizard.clearState()
                _reviewEvent.emit(ReviewEvent.Success)
            } catch (e: Exception) {
                Timber.e(e)
                _reviewEvent.emit(ReviewEvent.UnknownError)
            } finally {
                _reviewState.value = ReviewState.Idle
            }
        }

    }

    sealed class ReviewState {
        object Idle : ReviewState()
        object Loading : ReviewState()
    }

    sealed class ReviewEvent {
        object ZeroRatingError : ReviewEvent()
        object UnknownError : ReviewEvent()
        object Success : ReviewEvent()
    }

}