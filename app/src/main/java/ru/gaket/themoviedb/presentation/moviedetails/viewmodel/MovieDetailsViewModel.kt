package ru.gaket.themoviedb.presentation.moviedetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.gaket.themoviedb.core.navigation.WebNavigator
import ru.gaket.themoviedb.domain.movies.MoviesInteractor
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val webNavigator: WebNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var movieId: Int = -1
    var title: String = "Empty"

    private var _argsState = MutableLiveData<MovieDetailsState>()
    val argsState: LiveData<MovieDetailsState>
        get() = _argsState

    init {
        movieId = savedStateHandle.get<Int>("ARG_MOVIE_ID") ?: -1
        title = savedStateHandle.get<String>("ARG_MOVIE_TITLE") ?: "Empty"
        _argsState.value = MovieDetailsState(movieId, title)
    }

    fun onBrowseMovieCLick() {
        webNavigator.navigateTo(movieId)
    }
}