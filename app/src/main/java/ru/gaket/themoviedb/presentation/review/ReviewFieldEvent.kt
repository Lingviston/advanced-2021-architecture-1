package ru.gaket.themoviedb.presentation.review

import android.view.View
import com.google.android.material.snackbar.Snackbar
import ru.gaket.themoviedb.R

sealed class ReviewFieldEvent {
    object EmptyField : ReviewFieldEvent()
    object Success : ReviewFieldEvent()
}

fun ReviewFieldEvent.process(view: View, doOnSuccess: () -> Unit) {
    when (this) {
        ReviewFieldEvent.EmptyField -> Snackbar.make(
            view,
            R.string.review_error_should_not_be_empty,
            Snackbar.LENGTH_SHORT
        ).show()
        ReviewFieldEvent.Success -> doOnSuccess()
    }
}