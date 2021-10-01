package ru.gaket.themoviedb.presentation.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import ru.gaket.themoviedb.domain.movies.MoviesInteractor
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.EmptyQuery
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.EmptyResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.ErrorResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.ValidResult
import ru.gaket.themoviedb.util.OperationResult.Error
import ru.gaket.themoviedb.util.OperationResult.Success
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
        get() = _searchState

    private val queryFlow = MutableStateFlow("")

    val searchResult: LiveData<MoviesResult>
        get() = queryFlow
            .mapLatest(::handleQuery)
            .asLiveData(viewModelScope.coroutineContext)

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
        return when (val moviesResult = moviesInteractor.searchMovies(query)) {
            is Error -> ErrorResult(IllegalArgumentException("Search movies from server error!"))
            is Success -> if (moviesResult.result.isEmpty()) EmptyResult else ValidResult(moviesResult.result)
        }
    }
}
