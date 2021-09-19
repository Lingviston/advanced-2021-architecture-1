package ru.gaket.themoviedb.domain.movies

import ru.gaket.themoviedb.data.movies.local.MovieEntity

fun MovieEntity.toModel() = Movie(
	id = id,
	name = name,
	thumbnail = thumbnail
)