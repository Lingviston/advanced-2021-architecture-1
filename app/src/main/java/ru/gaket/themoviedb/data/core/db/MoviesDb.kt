package ru.gaket.themoviedb.data.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.gaket.themoviedb.data.genres.local.GenreEntity
import ru.gaket.themoviedb.data.genres.local.GenresDao

@Database(entities = [GenreEntity::class], version = 1)
abstract class MoviesDb : RoomDatabase() {

    abstract fun genresDao(): GenresDao
}