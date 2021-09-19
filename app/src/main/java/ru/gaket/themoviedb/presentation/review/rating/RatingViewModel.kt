package ru.gaket.themoviedb.presentation.review.rating

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RatingViewModel @Inject constructor() : ViewModel() {

    fun changeRating(rating: Int) {
        Timber.d("Rating Changed: $rating")
    }

    fun submit() {
        Timber.d("Submitted")
    }

}