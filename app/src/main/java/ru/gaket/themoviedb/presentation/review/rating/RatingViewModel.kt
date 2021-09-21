package ru.gaket.themoviedb.presentation.review.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val reviewState: LiveData<ReviewState>
        get() = _reviewState
    private val _reviewState = MutableLiveData<ReviewState>()

    fun submit(ratingNumber: Int) {
        val rating = Rating.mapToRating(ratingNumber)
        if (rating == null) {
            _reviewState.value = ReviewState.ZeroRating
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
                _reviewState.value = ReviewState.Success
            } catch (e: Exception) {
                Timber.e(e)
                _reviewState.value = ReviewState.Error
            }
        }

    }

    //TODO Decompose into events an state
    sealed class ReviewState {
        object Loading : ReviewState()
        object ZeroRating : ReviewState()
        object Success : ReviewState()
        object Error : ReviewState()
    }

}