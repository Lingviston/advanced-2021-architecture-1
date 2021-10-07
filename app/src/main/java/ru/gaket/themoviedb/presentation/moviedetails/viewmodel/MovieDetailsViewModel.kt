package ru.gaket.themoviedb.presentation.moviedetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.auth.AuthInteractor
import ru.gaket.themoviedb.domain.auth.isAuthorized
import ru.gaket.themoviedb.domain.auth.observeIsAuthorized
import ru.gaket.themoviedb.domain.movies.models.MovieWithReviews
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment.Companion.ARG_MOVIE_ID
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment.Companion.ARG_MOVIE_TITLE
import ru.gaket.themoviedb.util.Result
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val authInteractor: AuthInteractor,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val movieId: Long = savedStateHandle.get<Long>(ARG_MOVIE_ID) ?: error("Movie id was not passed!")
    private val title = savedStateHandle.get<String>(ARG_MOVIE_TITLE).orEmpty()

    private val _state = MutableLiveData<MovieDetailsState>(MovieDetailsState.Loading(title = title))
    val state: LiveData<MovieDetailsState> get() = _state

    private val _events = MutableSharedFlow<MovieDetailsEvent>()
    val events: LiveData<MovieDetailsEvent>
        get() = _events
            .asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            authInteractor.observeIsAuthorized()
                .flatMapLatest { isAuthorized ->
                    moviesRepository.observeMovieDetailsWithReviews(movieId)
                        .map { details -> details to isAuthorized }
                }
                .distinctUntilChanged()
                .collect { (detailsResult, isAuthorized) ->
                    _state.value = mapState(detailsResult, isAuthorized)
                }
        }
    }

    fun onReviewClick(review: MovieDetailsReview) =
        when (review) {
            is MovieDetailsReview.Add -> onAddReviewClick()
            is MovieDetailsReview.Existing -> Unit
        }

    private fun onAddReviewClick() {
        val event = if (authInteractor.isAuthorized()) {
            MovieDetailsEvent.OpenScreen.Review
        } else {
            MovieDetailsEvent.OpenScreen.Auth
        }
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}

private fun mapState(
    detailsResult: Result<MovieWithReviews, Throwable>,
    isAuthorized: Boolean,
): MovieDetailsState =
    when (detailsResult) {
        is Result.Success -> mapSuccessState(detailsResult.result, isAuthorized)
        is Result.Error -> MovieDetailsState.Error
    }

private fun mapSuccessState(
    details: MovieWithReviews,
    isAuthorized: Boolean,
): MovieDetailsState.Result {
    val allReviews = mutableListOf<MovieDetailsReview>()

    val myReviewOrAddButton = if (details.myReview != null) {
        MovieDetailsReview.Existing.My(review = details.myReview.review)
    } else {
        MovieDetailsReview.Add(isAuthorized = isAuthorized)
    }

    val someoneReviews = details.someoneElseReviews
        .map(MovieDetailsReview.Existing::Someone)

    allReviews.add(myReviewOrAddButton)
    allReviews.addAll(someoneReviews)

    return MovieDetailsState.Result(
        movie = details.movie,
        allReviews = allReviews
    )
}
