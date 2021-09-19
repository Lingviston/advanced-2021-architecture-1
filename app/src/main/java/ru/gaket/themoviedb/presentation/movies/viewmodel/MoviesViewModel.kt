package ru.gaket.themoviedb.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import ru.gaket.themoviedb.core.navigation.WebNavigator
import ru.gaket.themoviedb.domain.movies.Movie
import ru.gaket.themoviedb.domain.movies.MoviesInteractor
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
	private val moviesInteractor: MoviesInteractor,
	private val webNavigator: WebNavigator,
	private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var _argsTestValue = MutableLiveData<Int>()
    val argsTestValue: LiveData<Int>
        get() = _argsTestValue

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
        get() = _searchState

    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @FlowPreview
    private val _searchResult = queryChannel
        .asFlow()
        .debounce(500)
        .onEach {
            _searchState.value = Loading
        }
        .mapLatest { query ->
            if (query.isEmpty()) {
                EmptyQuery
            } else {
                try {
                    val result = moviesInteractor.searchMovies(query)
                    if (result.isEmpty()) {
                        EmptyResult
                    } else {
                        ValidResult(result)
                    }
                } catch (e: Throwable) {
                    if (e is CancellationException) {
                        throw e
                    } else {
                        Timber.w(e)
                        ErrorResult(e)
                    }
                }
            }
        }
        .onEach {
            _searchState.value = Ready
        }
        .catch { emit(TerminalError) }
        .asLiveData(viewModelScope.coroutineContext)

    @FlowPreview
    val searchResult: LiveData<MoviesResult>
        get() = _searchResult

    init {
        _argsTestValue.value = savedStateHandle.get<Int>("ARG_TEST_VALUE") ?: -1
    }

    fun onMovieAction(movie: Movie) {
        webNavigator.navigateTo(movie.id)
    }
}