package ru.gaket.themoviedb.presentation.review

import androidx.fragment.app.FragmentManager
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.presentation.review.rating.RatingFragment
import ru.gaket.themoviedb.presentation.review.whatnotliked.WhatNotLikeFragment
import javax.inject.Inject

interface ReviewNavigator {
    //TODO Think what to do with init method
    fun init(fragmentManager: FragmentManager)
    fun navigateToWhatNotLikeFragment()
    fun navigateToRatingFragment()
}

class ReviewNavigatorImplementation @Inject constructor() : ReviewNavigator {

    private lateinit var fragmentManager: FragmentManager

    override fun init(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    override fun navigateToWhatNotLikeFragment() {
        fragmentManager
            .beginTransaction()
            .replace(R.id.container, WhatNotLikeFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun navigateToRatingFragment() {
        fragmentManager
            .beginTransaction()
            .replace(R.id.container, RatingFragment())
            .addToBackStack(null)
            .commit()
    }
}