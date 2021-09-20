package ru.gaket.themoviedb.data.genres

import ru.gaket.themoviedb.data.genres.local.GenreEntity
import ru.gaket.themoviedb.data.genres.remote.GenreDto

fun GenreDto.toEntity() = GenreEntity(
    id = id,
    name = name
)