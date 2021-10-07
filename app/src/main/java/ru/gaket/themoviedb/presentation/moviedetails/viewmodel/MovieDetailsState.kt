package ru.gaket.themoviedb.presentation.moviedetails.viewmodel

import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview

sealed class MovieDetailsState {

    data class Loading(val title: String) : MovieDetailsState()

    data class Result(
        val movie: Movie,
        val allReviews: List<MovieDetailsReview>,
    ) : MovieDetailsState()

    object Error : MovieDetailsState()
}

sealed class MovieDetailsEvent {

    sealed class OpenScreen : MovieDetailsEvent() {

        object Review : MovieDetailsEvent.OpenScreen()

        object Auth : MovieDetailsEvent.OpenScreen()
    }
}