package ru.gaket.themoviedb.data.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.gaket.themoviedb.data.genres.local.GenreEntity
import ru.gaket.themoviedb.data.genres.local.GenresDao
import ru.gaket.themoviedb.data.movies.local.MovieEntity
import ru.gaket.themoviedb.data.movies.local.MoviesDao

@Database(
    entities = [
        GenreEntity::class,
        MovieEntity::class
    ],
    version = 2
)
abstract class MoviesDb : RoomDatabase() {

    abstract fun genresDao(): GenresDao
    abstract fun moviesDao(): MoviesDao
}