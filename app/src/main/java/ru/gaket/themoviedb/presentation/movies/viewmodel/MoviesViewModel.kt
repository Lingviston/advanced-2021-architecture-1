package ru.gaket.themoviedb.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import ru.gaket.themoviedb.domain.movies.MoviesInteractor
import ru.gaket.themoviedb.util.OperationResult
import ru.gaket.themoviedb.util.UnitTestable
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Inject

@UnitTestable
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
        get() = _searchState

    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @FlowPreview
    private val _searchResult = queryChannel.asFlow()
        .debounce(500)
        .onEach { _searchState.value = Loading }
        .mapLatest(::onNewQuery)
        .onEach { _searchState.value = Ready }
        .catch { emit(TerminalError) }
        .asLiveData(viewModelScope.coroutineContext)

    @FlowPreview
    val searchResult: LiveData<MoviesResult>
        get() = _searchResult

    private suspend fun onNewQuery(query: String): MoviesResult {
        return if (query.isEmpty()) {
            EmptyQuery
        } else {
            try {
                when (val value = moviesInteractor.searchMovies(query)) {
                    is OperationResult.Error -> ErrorResult(IllegalArgumentException("Search movies from server error"))
                    is OperationResult.Success -> if (value.result.isEmpty()) EmptyResult else ValidResult(value.result)
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
}
