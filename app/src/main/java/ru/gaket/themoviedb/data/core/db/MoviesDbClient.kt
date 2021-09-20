package ru.gaket.themoviedb.data.core.db

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.gaket.themoviedb.data.genres.local.GenresDao
import ru.gaket.themoviedb.data.movies.local.MoviesDao
import javax.inject.Inject

interface MoviesDbClient {

    fun genresDao(): GenresDao
    fun moviesDao(): MoviesDao
}

class MoviesDbClientImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : MoviesDbClient {


    // todo: [Sergey] move to constants
    private val database = Room.databaseBuilder(
        context,
        MoviesDb::class.java,
        "movies_database"
    ).fallbackToDestructiveMigration()
        .build()

    override fun genresDao(): GenresDao = database.genresDao()
    override fun moviesDao(): MoviesDao = database.moviesDao()
}
