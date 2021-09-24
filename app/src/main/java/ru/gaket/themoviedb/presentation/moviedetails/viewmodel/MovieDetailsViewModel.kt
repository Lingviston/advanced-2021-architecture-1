package ru.gaket.themoviedb.presentation.moviedetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.gaket.themoviedb.core.navigation.WebNavigator
import ru.gaket.themoviedb.domain.movies.MoviesInteractor
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment.Companion.ARG_MOVIE_ID
import ru.gaket.themoviedb.util.UnitTestable
import javax.inject.Inject

@UnitTestable
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val webNavigator: WebNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val movieId = savedStateHandle.get<Long>(ARG_MOVIE_ID) ?: -1
    private val title = savedStateHandle.get<String>(MovieDetailsFragment.ARG_MOVIE_TITLE).orEmpty()

    private var _argsState = MutableLiveData<MovieDetailsState>()
    val argsState: LiveData<MovieDetailsState>
        get() = _argsState

    init {
        _argsState.value = MovieDetailsState(movieId, title)
    }

    fun onBrowseMovieCLick() {
        webNavigator.navigateTo(movieId)
    }
}
