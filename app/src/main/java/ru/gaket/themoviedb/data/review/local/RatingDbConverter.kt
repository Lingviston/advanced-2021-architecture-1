package ru.gaket.themoviedb.data.review.local

import androidx.room.TypeConverter
import ru.gaket.themoviedb.domain.review.Rating
import ru.gaket.themoviedb.util.UnitTestable

@UnitTestable
internal class RatingDbConverter {

    @TypeConverter
    fun fromIntToRating(value: Int?): Rating? = value?.let { nonNullValue ->
        Rating.values()
            .firstOrNull { syncStatus -> syncStatus.starsCount == nonNullValue }
    }

    @TypeConverter
    fun fromRatingToInt(value: Rating?): Int? = value?.starsCount
}