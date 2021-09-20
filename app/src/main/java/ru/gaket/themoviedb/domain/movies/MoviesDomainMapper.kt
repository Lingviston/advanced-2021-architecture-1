package ru.gaket.themoviedb.domain.movies

import ru.gaket.themoviedb.data.movies.local.MovieEntity
import ru.gaket.themoviedb.data.movies.local.SearchMovieEntity
import ru.gaket.themoviedb.domain.movies.models.Movie
import ru.gaket.themoviedb.domain.movies.models.SearchMovie

fun SearchMovieEntity.toModel() = SearchMovie(
    id = id,
    title = title,
    thumbnail = thumbnail,
)

fun MovieEntity.toModel() = Movie(
    id = id,
    title = title,
    overview = overview,
    allowedAge = allowedAge,
    rating = rating,
    reviewsCounter = reviewsCounter,
    popularity = popularity,
    releaseDate = releaseDate,
    duration = duration,
    budget = budget,
    revenue = revenue,
    status = status,
    genres = genres,
    homepage = homepage,
    thumbnail = thumbnail,
    hasReview = hasReview,
    reviewId = reviewId,
)