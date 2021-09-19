package ru.gaket.themoviedb.presentation.review.whatnotliked

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WhatNotLikeViewModel @Inject constructor() : ViewModel() {

    fun submitInfo(whatDidNotLike: String) {
        Timber.d(whatDidNotLike)
    }

}