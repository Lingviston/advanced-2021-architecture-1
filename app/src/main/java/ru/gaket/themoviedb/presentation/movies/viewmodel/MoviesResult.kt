package ru.gaket.themoviedb.presentation.movies.viewmodel

import ru.gaket.themoviedb.domain.movies.models.SearchMovie
import ru.gaket.themoviedb.domain.movies.models.SearchMovieWithMyReview

/**
 * Class containing the result of the [SearchMovie] request
 */
sealed class MoviesResult {

    class ValidResult(val result: List<SearchMovieWithMyReview>) : MoviesResult()
    object EmptyResult : MoviesResult()
    object EmptyQuery : MoviesResult()
    class ErrorResult(val e: Throwable) : MoviesResult()
}
