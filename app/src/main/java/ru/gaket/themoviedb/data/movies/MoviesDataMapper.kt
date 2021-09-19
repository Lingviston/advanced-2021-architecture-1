package ru.gaket.themoviedb.data.movies

import ru.gaket.themoviedb.data.movies.local.MovieEntity
import ru.gaket.themoviedb.data.movies.remote.MovieDto

fun MovieDto.toEntity(baseImageUrl: String) = MovieEntity(
	id = id,
	name = title,
	thumbnail = getPosterUrl(baseImageUrl, posterPath)
)

private fun getPosterUrl(baseImageUrl: String, posterPath: String?) =
	"${baseImageUrl}${posterPath}"