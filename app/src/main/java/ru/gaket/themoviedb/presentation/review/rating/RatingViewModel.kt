package ru.gaket.themoviedb.presentation.review.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.presentation.review.ReviewWizard
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val reviewWizard: ReviewWizard,
    private val moviesRepository: MoviesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    //TODO Add validation and loading
    fun submit(rating: Rating) {
        reviewWizard.setRating(rating)
        val (userId, userEmail) = authRepository.currentUser ?: error("User is not signed in")

        viewModelScope.launch {
            moviesRepository.addReview(
                reviewWizard.buildReview(),
                userId,
                userEmail
            )
            reviewWizard.clearState()
        }

    }

}