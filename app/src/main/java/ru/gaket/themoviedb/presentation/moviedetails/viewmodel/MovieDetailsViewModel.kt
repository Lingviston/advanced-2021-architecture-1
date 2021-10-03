package ru.gaket.themoviedb.presentation.moviedetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.WebNavigator
import ru.gaket.themoviedb.domain.movies.MoviesInteractor
import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.domain.review.SomeoneReview
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsEvent
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsEvent.OpenAddReviewScreenEvent
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsEvent.ShowErrorEvent
import ru.gaket.themoviedb.presentation.moviedetails.model.toMovieDetailsReview
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment.Companion.ARG_MOVIE_ID
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment.Companion.ARG_MOVIE_TITLE
import ru.gaket.themoviedb.util.get
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val reviewRepository: ReviewRepository,
    private val webNavigator: WebNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val movieId = savedStateHandle.get<Long>(ARG_MOVIE_ID) ?: -1
    private val title = savedStateHandle.get<String>(ARG_MOVIE_TITLE).orEmpty()

    private var _state = MutableLiveData<MovieDetailsState>()
    val state: LiveData<MovieDetailsState> get() = _state

    private var _events = MutableSharedFlow<MovieDetailsEvent>()
    val events: SharedFlow<MovieDetailsEvent> get() = _events.asSharedFlow()

    init {
        _state.value = MovieDetailsState(title = title)

        viewModelScope.launch {
            moviesInteractor.getMovieDetails(movieId).get(
                onSuccess = ::handleMovie,
                onError = {
                    ShowErrorEvent(R.string.error_getting_movie_info).publish()
                    reduceState { copy(isLoadingInfo = false) }
                },
            )
        }

        viewModelScope.launch {
            reviewRepository.getSomeoneReviews(movieId).get(
                onSuccess = ::handleSomeoneReviews,
                onError = {
                    ShowErrorEvent(R.string.error_getting_movie_info).publish()
                    reduceState { copy(isLoadingReviews = false) }
                },
            )
        }

        viewModelScope.launch {
            reviewRepository.getMyReviews(movieId).collect(::handleMyReview)
        }
    }

    fun onBrowseMovieClick() = webNavigator.navigateTo(movieId)

    fun onReviewClick() = Unit

    fun onAddReviewClick() = OpenAddReviewScreenEvent(movieId).publish()

    @Synchronized
    private fun reduceState(reducer: MovieDetailsState.() -> MovieDetailsState) {
        _state.value = (_state.value ?: MovieDetailsState()).reducer()
    }

    private fun handleMovie(movie: Movie) = reduceState {
        copy(
            isLoadingInfo = false,
            year = movie.releaseDate.toDate()?.getCalendarYear()?.toString().orEmpty(),
            overview = movie.overview,
            genres = movie.genres,
            rating = movie.rating.toString(),
            posterUrl = movie.thumbnail,
        )
    }

    private fun String.toDate(): Date? = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(this)

    private fun Date.getCalendarYear(): Int = Calendar.getInstance().run {
        time = this@getCalendarYear
        get(Calendar.YEAR)
    }

    private fun handleSomeoneReviews(reviews: List<SomeoneReview>) = reduceState {
        copy(
            isLoadingReviews = false,
            someoneReviews = reviews.map { it.toMovieDetailsReview() },
        )
    }

    private fun handleMyReview(review: MyReview) = reduceState {
        copy(myReview = review.toMovieDetailsReview())
    }

    private fun MovieDetailsEvent.publish() = viewModelScope.launch {
        _events.emit(this@publish)
    }
}
