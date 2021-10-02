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
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.END_STATE
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.RATING
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.WHAT_LIKED
import ru.gaket.themoviedb.presentation.review.ReviewViewModel.ReviewState.WHAT_NOT_LIKED
import ru.gaket.themoviedb.util.OperationResult.Error
import ru.gaket.themoviedb.util.OperationResult.Success
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val reviewRepository: ReviewRepository,
    savedState: SavedStateHandle,
) : ViewModel() {

    val currentReview: LiveData<Pair<Movie, ReviewFormModel>?>

    private val _reviewState = MutableStateFlow(WHAT_LIKED)
    val reviewState: LiveData<ReviewState> get() = _reviewState.asLiveData(viewModelScope.coroutineContext)

    init {
        val movieId: MovieId = savedState.get<MovieId>(ARG_MOVIE_ID) ?: error("You need to provide $ARG_MOVIE_ID")

        viewModelScope.launch {
            reviewRepository.clearState()
            reviewRepository.setMovieId(movieId)
        }

        currentReview = flow {
            val moviePair = when (val movie = moviesRepository.getMovieDetails(movieId)) {
                is Success -> movie.result
                is Error -> null
            }
            emit(moviePair)
        }
            .combine(reviewRepository.reviewState) { movie: Movie?, review: ReviewFormModel -> movie?.let { movie to review } }
            .asLiveData(viewModelScope.coroutineContext)
    }

    fun nextState() {
        _reviewState.value = when (val state = _reviewState.value) {
            WHAT_LIKED -> WHAT_NOT_LIKED
            WHAT_NOT_LIKED -> RATING
            RATING -> END_STATE
            else -> throw IllegalStateException("You can't use goNext() at $state")
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
                else -> throw IllegalStateException("You can't use previousState() at $state")
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