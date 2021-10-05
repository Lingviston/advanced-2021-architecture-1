package ru.gaket.themoviedb.presentation.moviedetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.data.movies.MoviesRepository
import ru.gaket.themoviedb.domain.auth.AuthInteractor
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

    val movieId: Long = savedStateHandle.get<Long>(ARG_MOVIE_ID)!!
    private val title = savedStateHandle.get<String>(ARG_MOVIE_TITLE).orEmpty()

    private val _state = MutableLiveData<MovieDetailsState>(MovieDetailsState.Loading(title = title))
    val state: LiveData<MovieDetailsState> get() = _state

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

    fun onReviewClick() = Unit
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

    val myReview = MovieDetailsReview.My(
        review = details.myReview?.review,
        isAuthorized = isAuthorized
    )
    val someoneReviews = details.someoneElseReviews.map(MovieDetailsReview::Someone)

    allReviews.add(myReview)
    allReviews.addAll(someoneReviews)

    return MovieDetailsState.Result(
        movie = details.movie,
        allReviews = allReviews
    )
}