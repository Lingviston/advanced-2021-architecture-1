package ru.gaket.themoviedb.core.navigation

import androidx.fragment.app.Fragment
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.presentation.auth.view.AuthFragment
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment
import ru.gaket.themoviedb.presentation.movies.view.MoviesFragment
import ru.gaket.themoviedb.presentation.review.rating.RatingFragment
import ru.gaket.themoviedb.presentation.review.whatliked.WhatLikeFragment
import ru.gaket.themoviedb.presentation.review.whatnotliked.WhatNotLikeFragment

interface Screen {

    fun destination(): Fragment
}

class MoviesScreen : Screen {

    override fun destination(): Fragment = MoviesFragment.newInstance()
}

class MovieDetailsScreen(
    private val movieId: Long,
    private val title: String,
) : Screen {

    override fun destination(): Fragment = MovieDetailsFragment.newInstance(movieId, title)
}

class AuthScreen : Screen {

    override fun destination(): Fragment = AuthFragment.newInstance()
}

sealed class ReviewScreen : Screen {
    data class LikedScreen(
        private val movieId: MovieId
    ) : ReviewScreen() {
        override fun destination(): Fragment = WhatLikeFragment.newInstance(movieId)
    }

    object NotLikedScreen : ReviewScreen() {
        override fun destination(): Fragment = WhatNotLikeFragment()
    }

    object RatingScreen : ReviewScreen() {
        override fun destination(): Fragment = RatingFragment()
    }
}
