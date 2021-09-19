package ru.gaket.themoviedb.data.movies

import ru.gaket.themoviedb.data.movies.local.SearchMovieEntity
import ru.gaket.themoviedb.data.movies.remote.SearchMovieDto

fun SearchMovieDto.toEntity(baseImageUrl: String) = SearchMovieEntity(
    id = id,
    name = title,
    thumbnail = getPosterUrl(baseImageUrl, posterPath)
)

private fun getPosterUrl(baseImageUrl: String, posterPath: String?) =
    "${baseImageUrl}${posterPath}"