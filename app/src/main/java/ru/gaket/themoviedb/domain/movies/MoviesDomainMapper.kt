package ru.gaket.themoviedb.domain.movies

import ru.gaket.themoviedb.data.movies.db.MovieEntity

fun MovieEntity.toModel() = Movie(
	id = id,
	name = name,
	thumbnail = thumbnail
)