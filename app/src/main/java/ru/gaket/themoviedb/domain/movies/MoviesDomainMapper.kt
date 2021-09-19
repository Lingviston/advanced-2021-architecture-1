package ru.gaket.themoviedb.domain.movies

import ru.gaket.themoviedb.data.movies.local.SearchMovieEntity
import ru.gaket.themoviedb.domain.movies.search.SearchMovie

fun SearchMovieEntity.toModel() = SearchMovie(
    id = id,
    name = name,
    thumbnail = thumbnail
)