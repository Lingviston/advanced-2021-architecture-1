package ru.gaket.themoviedb.data.review.remote

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import ru.gaket.themoviedb.domain.auth.User
import ru.gaket.themoviedb.domain.review.models.ReviewDraft
import ru.gaket.themoviedb.domain.review.models.MyReview
import ru.gaket.themoviedb.domain.review.models.Rating
import ru.gaket.themoviedb.domain.review.models.Review
import ru.gaket.themoviedb.domain.review.models.SomeoneReview

internal const val MOVIES_COLLECTION = "movies"
internal const val REVIEWS_COLLECTION = "reviews"

internal const val AUTHOR_ID = "author_id"
internal const val AUTHOR_EMAIL = "author_email"
internal const val LIKED = "liked"
internal const val DISLIKED = "disliked"
internal const val RATING = "rating"

internal fun DocumentSnapshot.toMyReview(): MyReview? =
    this.toReview()?.let { review ->
        MyReview(
            movieId = requireNotNull(this.reference.parent.parent).id.toLong(),
            review = review
        )
    }

internal fun DocumentSnapshot.toSomeoneReview(): SomeoneReview? =
    this.toReview()?.let { review ->
        SomeoneReview(
            author = requireNotNull(User.Email.createIfValid(value = this.getString(AUTHOR_EMAIL))),
            review = review
        )
    }

internal fun DocumentSnapshot.toReview(): Review? =
    this.getLong(RATING)
        ?.toInt()
        ?.let { ratingLong -> Rating.values().firstOrNull { rating -> rating.starsCount == ratingLong } }
        ?.let { rating ->
            Review(
                id = Review.Id(value = this.id),
                liked = requireNotNull(this.getString(LIKED)),
                disliked = requireNotNull(this.getString(DISLIKED)),
                rating = rating
            )
        }

internal fun ReviewDraft.toDatastoreMap(
    authorId: User.Id,
    authorEmail: User.Email,
): Map<Any, Any> =
    mapOf(
        AUTHOR_ID to authorId.value,
        AUTHOR_EMAIL to authorEmail.value,
        LIKED to this.liked,
        DISLIKED to this.disliked,
        RATING to this.rating.starsCount
    )

internal fun ReviewDraft.toMyReview(documentReference: DocumentReference): MyReview =
    MyReview(
        movieId = this.movieId,
        review = Review(
            id = Review.Id(documentReference.id),
            liked = this.liked,
            disliked = this.disliked,
            rating = this.rating
        )
    )
