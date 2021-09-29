package ru.gaket.themoviedb.core.navigation

import androidx.fragment.app.Fragment
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.presentation.review.rating.RatingFragment
import ru.gaket.themoviedb.presentation.review.whatliked.WhatLikeFragment
import ru.gaket.themoviedb.presentation.review.whatnotliked.WhatNotLikeFragment

data class LikedScreen(
    private val movieId: MovieId,
) : Screen {

    override fun destination(): Fragment = WhatLikeFragment.newInstance(movieId)
}

class NotLikedScreen : Screen {

    override fun destination(): Fragment = WhatNotLikeFragment()
}

class RatingScreen : Screen {

    override fun destination(): Fragment = RatingFragment()
}
