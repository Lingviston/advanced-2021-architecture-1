package ru.gaket.themoviedb.presentation.review.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    //TODO Clarify what better way to send events to View in this project (or use LiveData???)
    val navigateBackEvent: Flow<Unit>
        get() = _navigateBackEvent.asSharedFlow()
    private val _navigateBackEvent = MutableSharedFlow<Unit>()

    //TODO [Vlad] Add validation and loading
    fun submit(rating: Rating) {
        reviewWizard.setRating(rating)

        viewModelScope.launch {
            try {
                val (userId, userEmail) = authRepository.currentUser
                    ?: error("User is not signed in")
                moviesRepository.addReview(
                    reviewWizard.buildReview(),
                    userId,
                    userEmail
                )
                reviewWizard.clearState()
                _navigateBackEvent.emit(Unit)
            } catch (e: Exception) {
                //TODO [Vlad] Show error message
                Timber.e(e)
            }
        }

    }

}