package ru.gaket.themoviedb.presentation.moviedetails.viewmodel

import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview.MyReview
import ru.gaket.themoviedb.presentation.moviedetails.model.MovieDetailsReview.SomeoneReview

data class MovieDetailsState(
    val isLoadingInfo: Boolean = true,
    val isLoadingReviews: Boolean = true,
    val title: String = "",
    val year: String = "",
    val overview: String = "",
    val genres: String = "",
    val rating: String = "",
    val posterUrl: String? = "",
    val someoneReviews: List<SomeoneReview> = emptyList(),
    val myReview: MyReview = MyReview(),
) {

    val allReviews: List<MovieDetailsReview>
        get() = mutableListOf<MovieDetailsReview>().apply {
            add(myReview)
            addAll(someoneReviews)
        }
}
