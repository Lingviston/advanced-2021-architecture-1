package ru.gaket.themoviedb.presentation.review.whatnotliked

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.gaket.themoviedb.presentation.review.ReviewWizard
import javax.inject.Inject

@HiltViewModel
class WhatNotLikeViewModel @Inject constructor(
    private val reviewWizard: ReviewWizard
) : ViewModel() {

    fun submitInfo(whatDidNotLike: String) {
        reviewWizard.setWhatDidNotLike(whatDidNotLike)
    }

}