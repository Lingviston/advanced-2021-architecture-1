package ru.gaket.themoviedb.presentation.review.whatliked

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.gaket.themoviedb.presentation.review.ReviewNavigator
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WhatLikeViewModel @Inject constructor(
    private val reviewNavigator: ReviewNavigator
) : ViewModel() {

    fun submitInfo(whatLike: String) {
        Timber.d(whatLike)
        reviewNavigator.navigateToWhatNotLikeFragment()
    }

}