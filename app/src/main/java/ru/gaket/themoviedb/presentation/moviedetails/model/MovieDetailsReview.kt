package ru.gaket.themoviedb.presentation.moviedetails.model

import ru.gaket.themoviedb.domain.review.models.Review
import ru.gaket.themoviedb.domain.review.models.SomeoneReview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

sealed class MovieDetailsReview {

    abstract val review: Review?

    data class My(
        override val review: Review?,
        val isAuthorized: Boolean,
    ) : MovieDetailsReview()

    data class Someone(
        val info: SomeoneReview,
    ) : MovieDetailsReview() {

        override val review: Review get() = this.info.review
    }
}

val MovieDetailsReview.My.isPosted: Boolean
    get() = (this.review != null)

private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)

fun String.getCalendarYear(): Int? = try {
    DATE_FORMAT.parse(this)
} catch (e: Throwable) {
    null
}?.getCalendarYear()

private fun Date.getCalendarYear(): Int = Calendar.getInstance().run {
    time = this@getCalendarYear
    get(Calendar.YEAR)
}