package ru.gaket.themoviedb.data.review.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gaket.themoviedb.domain.movies.models.MovieId
import ru.gaket.themoviedb.domain.review.MyReview
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.domain.review.Review

@Entity(tableName = MyReviewEntity.TABLE_NAME)
data class MyReviewEntity(

    @PrimaryKey
    @ColumnInfo(name = REVIEW_ID)
    val reviewId: String,

    @ColumnInfo(name = REVIEW_MOVIE_ID)
    val reviewMovieId: MovieId,

    @ColumnInfo(name = LIKED)
    val liked: String,

    @ColumnInfo(name = DISLIKED)
    val disliked: String,

    @ColumnInfo(name = RATING)
    val rating: Rating
) {
    companion object {
        const val TABLE_NAME = "my_reviews"

        const val REVIEW_ID = "review_id"
        const val REVIEW_MOVIE_ID = "reviewMovieId"
        const val LIKED = "liked"
        const val DISLIKED = "disliked"
        const val RATING = "rating"
    }
}

internal fun MyReviewEntity.toModel(): MyReview =
    MyReview(
        movieId = this.reviewMovieId,
        review = Review(
            id = Review.Id(value = this.reviewId),
            liked = this.liked,
            disliked = this.disliked,
            rating = this.rating
        )
    )

internal fun MyReview.toEntity(): MyReviewEntity =
    MyReviewEntity(
        reviewMovieId = this.movieId,
        reviewId = this.review.id.value,
        liked = this.review.liked,
        disliked = this.review.disliked,
        rating = this.review.rating
    )