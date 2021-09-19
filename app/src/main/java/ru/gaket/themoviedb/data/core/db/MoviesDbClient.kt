package ru.gaket.themoviedb.data.core.db

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.gaket.themoviedb.data.genres.local.GenresDao

interface MoviesDbClient {

    fun genresDao(): GenresDao
}

class MoviesDbClientImpl(
	@ApplicationContext private val context: Context,
) : MoviesDbClient {

    private val database = Room.databaseBuilder(
        context,
        MoviesDb::class.java,
        "movies_database"
    ).fallbackToDestructiveMigration()
        .build()

    override fun genresDao(): GenresDao = database.genresDao()
}