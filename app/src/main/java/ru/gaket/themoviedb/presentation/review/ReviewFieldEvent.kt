package ru.gaket.themoviedb.presentation.review

import android.view.View
import com.google.android.material.snackbar.Snackbar
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent.EMPTY_FIELD
import ru.gaket.themoviedb.presentation.review.ReviewFieldEvent.SUCCESS

enum class ReviewFieldEvent {
    EMPTY_FIELD,
    SUCCESS,
}

fun ReviewFieldEvent.process(view: View, doOnSuccess: () -> Unit) {
    when (this) {
        EMPTY_FIELD -> Snackbar.make(
            view,
            R.string.review_error_should_not_be_empty,
            Snackbar.LENGTH_SHORT
        ).show()
        SUCCESS -> doOnSuccess()
    }
}