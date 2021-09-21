package ru.gaket.themoviedb.presentation.review.whatliked

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.presentation.review.ReviewWizard
import javax.inject.Inject

@HiltViewModel
class WhatLikeViewModel @Inject constructor(
    private val reviewWizard: ReviewWizard,
    savedState: SavedStateHandle
) : ViewModel() {

    init {
        val movieId: MovieId = savedState.get<MovieId>(ARG_MOVIE_ID)
            ?: error("You need to provide $ARG_MOVIE_ID")
        reviewWizard.start(movieId)
    }

    fun submitInfo(whatLike: String) {
        reviewWizard.setWhatLike(whatLike)
    }

    companion object {
        const val ARG_MOVIE_ID = "ARG_MOVIE_ID"
    }

}