package ru.gaket.themoviedb.presentation.movies.viewmodel

import ru.gaket.themoviedb.domain.movies.models.SearchMovie
import ru.gaket.themoviedb.domain.movies.models.SearchMovieWithMyReview

/**
 * Class containing the result of the [SearchMovie] request
 */
sealed class MoviesResult {

    object Loading : MoviesResult()
    object EmptyResult : MoviesResult()
    object EmptyQuery : MoviesResult()
    data class SuccessResult(val result: List<SearchMovieWithMyReview>) : MoviesResult()
    data class ErrorResult(val e: Throwable) : MoviesResult()
}
