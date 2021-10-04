package ru.gaket.themoviedb.presentation.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.models.ReviewFormModel
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.presentation.review.MovieWithReviewViewState.MovieWithReview
import ru.gaket.themoviedb.presentation.review.MovieWithReviewViewState.NoMovie
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.END_STATE
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.RATING
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.WHAT_LIKED
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.WHAT_NOT_LIKED
import ru.gaket.themoviedb.util.Result
import ru.gaket.themoviedb.util.Result.Error
import ru.gaket.themoviedb.util.Result.Success
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val reviewRepository: ReviewRepository,
    savedState: SavedStateHandle,
) : ViewModel() {

    val currentReview: LiveData<MovieWithReviewViewState>

    private val _reviewState = MutableStateFlow(WHAT_LIKED)
    val reviewState: LiveData<ReviewState> get() = _reviewState.asLiveData(viewModelScope.coroutineContext)

    init {
        val movieId: MovieId = savedState.get<MovieId>(ARG_MOVIE_ID) ?: error("You need to provide $ARG_MOVIE_ID")

        viewModelScope.launch {
            reviewRepository.clearState()
            reviewRepository.setMovieId(movieId)
        }

        currentReview = flow { emit(moviesRepository.getMovieDetails(movieId)) }
            .combine(reviewRepository.reviewState) { movieResult: Result<Movie, Throwable>, review: ReviewFormModel ->
                when (movieResult) {
                    is Success -> MovieWithReview(movieResult.result, review)
                    is Error -> NoMovie
                }
            }
            .asLiveData(viewModelScope.coroutineContext)
    }

    fun nextState() {
        _reviewState.value = when (val state = _reviewState.value) {
            WHAT_LIKED -> WHAT_NOT_LIKED
            WHAT_NOT_LIKED -> RATING
            RATING -> END_STATE
            END_STATE -> throw IllegalStateException("You can't use goNext() at $state")
        }
    }

    fun previousState() {
        viewModelScope.launch {
            _reviewState.value = when (val state = _reviewState.value) {
                WHAT_LIKED -> {
                    reviewRepository.setWhatLike(null)
                    END_STATE
                }
                WHAT_NOT_LIKED -> {
                    reviewRepository.setWhatDidNotLike(null)
                    WHAT_LIKED
                }
                RATING -> {
                    reviewRepository.setRating(null)
                    WHAT_NOT_LIKED
                }
                END_STATE -> throw IllegalStateException("You can't use previousState() at $state")
            }
        }
    }

    enum class ReviewState {
        WHAT_LIKED,
        WHAT_NOT_LIKED,
        RATING,
        END_STATE,
    }

    companion object {

        const val ARG_MOVIE_ID = "ARG_MOVIE_ID"
    }
}
