package ru.gaket.themoviedb.presentation.movies.viewmodel

import ru.gaket.themoviedb.domain.movies.models.SearchMovie

/**
 * Class containing the result of the [SearchMovie] request
 */
sealed class MoviesResult
class ValidResult(val result: List<SearchMovie>) : MoviesResult()
object EmptyResult : MoviesResult()
object EmptyQuery : MoviesResult()
class ErrorResult(val e: Throwable) : MoviesResult()
object TerminalError : MoviesResult()