package ru.gaket.themoviedb.presentation.moviedetails.model

import androidx.annotation.StringRes
import ru.gaket.themoviedb.domain.movies.models.MovieId

sealed class MovieDetailsEvent {
    class ShowErrorEvent(@StringRes val errorMessageResId: Int) : MovieDetailsEvent()
    class OpenAuthScreenEvent : MovieDetailsEvent()
    class OpenAddReviewScreenEvent(val movieId: MovieId) : MovieDetailsEvent()
}
