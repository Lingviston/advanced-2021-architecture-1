package ru.gaket.themoviedb.data.movies.local

import androidx.room.TypeConverter
import ru.gaket.themoviedb.domain.review.Rating

internal class RatingDbConverter {

    @TypeConverter
    fun fromIntToRating(value: Int?): Rating? = value?.let { nonNullValue ->
        Rating.values()
            .firstOrNull { syncStatus -> syncStatus.starsCount == nonNullValue }
    }

    @TypeConverter
    fun fromRatingToInt(value: Rating?): Int? = value?.starsCount
}