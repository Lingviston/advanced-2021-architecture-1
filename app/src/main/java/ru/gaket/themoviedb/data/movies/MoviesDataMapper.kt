package ru.gaket.themoviedb.data.movies

import ru.gaket.themoviedb.data.genres.remote.GenreDto
import ru.gaket.themoviedb.data.movies.local.MovieEntity
import ru.gaket.themoviedb.data.movies.local.SearchMovieEntity
import ru.gaket.themoviedb.data.movies.remote.DetailsMovieDto
import ru.gaket.themoviedb.data.movies.remote.SearchMovieDto

fun SearchMovieDto.toEntity(baseImageUrl: String) = SearchMovieEntity(
    id = id,
    title = title,
    thumbnail = getPosterUrl(baseImageUrl, posterPath),
)

fun DetailsMovieDto.toEntity(
    baseImageUrl: String,
    hasReview: Boolean = false,
    reviewId: Int = 0
) = MovieEntity(
    id = id,
    imdbId = imdbId,
    title = title,
    overview = overview,
    allowedAge = normalizedAllowedAge(isAdult),
    rating = normalizedRating(rating),
    reviewsCounter = reviewsCounter,
    popularity = normalizedPopularity(popularity),
    releaseDate = releaseDate,
    duration = duration,
    budget = budget,
    revenue = revenue,
    status = status,
    genres = genresAsString(genres),
    homepage = homepage,
    thumbnail = getPosterUrl(baseImageUrl, posterPath),
    hasReview = hasReview,
    reviewId = reviewId,
    isUpdatedFromServer = true
)

private fun getPosterUrl(baseImageUrl: String, posterPath: String?) =
    "${baseImageUrl}${posterPath}"

private fun normalizedAllowedAge(isAdult: Boolean): String = if (isAdult) {
    AGE_ADULT
} else {
    AGE_CHILD
}

private fun normalizedRating(rating: Float): Int {
    return (rating / 2).toInt()
}

private fun normalizedPopularity(popularity: Float): Float {
    return "%.2f".format(popularity).toFloat()
}

private fun genresAsString(genres: List<GenreDto>) =
    genres.joinToString(transform = GenreDto::name)

private const val AGE_ADULT = "18+"
private const val AGE_CHILD = "13+"
