package ru.gaket.themoviedb.data.movies.local

import androidx.room.Embedded
import androidx.room.Relation

class MovieWithMyReview(
    @Embedded val user: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = MyReviewEntity.REVIEW_MOVIE_ID
    )
    val myReview: MyReviewEntity?
)