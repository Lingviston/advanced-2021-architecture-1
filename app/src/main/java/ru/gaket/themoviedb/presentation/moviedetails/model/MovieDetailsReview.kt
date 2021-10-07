package ru.gaket.themoviedb.presentation.moviedetails.model

import ru.gaket.themoviedb.domain.review.models.Review
import ru.gaket.themoviedb.domain.review.models.Review.Id
import ru.gaket.themoviedb.domain.review.models.SomeoneReview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

sealed class MovieDetailsReview {

    abstract val id: Review.Id?

    data class Add(
        val isAuthorized: Boolean,
    ) : MovieDetailsReview() {

        override val id: Review.Id? get() = null
    }

    sealed class Existing : MovieDetailsReview() {

        abstract val review: Review

        final override val id: Review.Id
            get() = review.id

        data class My(
            override val review: Review,
        ) : MovieDetailsReview.Existing()

        data class Someone(
            val info: SomeoneReview,
        ) : MovieDetailsReview.Existing() {

            override val review: Review get() = this.info.review
        }
    }
}

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