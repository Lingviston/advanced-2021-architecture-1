package ru.gaket.themoviedb.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import ru.gaket.themoviedb.data.movies.MoviesRepository
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.EmptyQuery
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.EmptyResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.ErrorResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.SuccessResult
import ru.gaket.themoviedb.util.Result.Error
import ru.gaket.themoviedb.util.Result.Success
import javax.inject.Inject

private const val TEXT_ENTERED_DEBOUNCE_MILLIS = 500L

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    private val _searchResult = MutableStateFlow<MoviesResult>(EmptyQuery)
    val searchResult: LiveData<MoviesResult>
        get() = _searchResult
            .asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            queryFlow
                .sample(TEXT_ENTERED_DEBOUNCE_MILLIS)
                .onEach { _searchResult.value = MoviesResult.Loading }
                .mapLatest(::handleQuery)
                .collect { state -> _searchResult.value = state }
        }
    }

    fun onNewQuery(query: String) {
        queryFlow.value = query
    }

    private suspend fun handleQuery(query: String): MoviesResult {
        return if (query.isEmpty()) {
            EmptyQuery
        } else {
            handleSearchMovie(query)
        }
    }

    private suspend fun handleSearchMovie(query: String): MoviesResult {
        return when (val moviesResult = moviesRepository.searchMoviesWithReviews(query)) {
            is Error -> ErrorResult(IllegalArgumentException("Search movies from server error!"))
            is Success -> if (moviesResult.result.isEmpty()) EmptyResult else SuccessResult(moviesResult.result)
        }
    }
}
