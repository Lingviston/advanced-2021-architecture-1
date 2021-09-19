package ru.gaket.themoviedb.presentation.review.whatliked

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WhatLikeViewModel @Inject constructor() : ViewModel() {
    fun submitInfo(whatLike: String) {
        Timber.d(whatLike)
    }
}